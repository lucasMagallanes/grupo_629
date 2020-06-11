//package com.projects.enzoftware.metalball
package com.example.helloandroidstudio

import android.app.Service
import android.bluetooth.BluetoothClass
import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Point
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
//import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Vibrator
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.example.helloandroidstudio.R

class BolaDeMetal : AppCompatActivity() , SensorEventListener {

    private var mSensorManager : SensorManager ?= null
    private var mAccelerometer : Sensor ?= null
    var ground : GroundView ?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)
        // get reference of the service
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        // focus in accelerometer
        mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        // setup the window
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            window.decorView.systemUiVisibility =   View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            View.SYSTEM_UI_FLAG_FULLSCREEN
            View.SYSTEM_UI_FLAG_IMMERSIVE
        }

        // set the view
        ground = GroundView(this)
        setContentView(ground)
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            ground!!.updateMe(event.values[1] , event.values[0])
        }
    }

    override fun onResume() {
        super.onResume()
        mSensorManager!!.registerListener(this,mAccelerometer,
            SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager!!.unregisterListener(this)
    }

    class DrawThread (surfaceHolder: SurfaceHolder , panel : GroundView) : Thread() {
        private var surfaceHolder :SurfaceHolder ?= null
        private var panel : GroundView ?= null
        private var run = false

        init {
            this.surfaceHolder = surfaceHolder
            this.panel = panel
        }

        fun setRunning(run : Boolean){
            this.run = run
        }

        override fun run() {
            var c: Canvas ?= null
            while (run){
                c = null
                try {
                    c = surfaceHolder!!.lockCanvas(null)
                    synchronized(surfaceHolder!!){
                        panel!!.draw(c)
                    }
                }finally {
                    if (c!= null){
                        surfaceHolder!!.unlockCanvasAndPost(c)
                    }
                }
            }
        }

    }

}


class GroundView(context: Context?) : SurfaceView(context), SurfaceHolder.Callback{

    // ball coordinates
    var cx : Float = 10.toFloat()
    var cy : Float = 10.toFloat()

    // last position increment

    var lastGx : Float = 0.toFloat()
    var lastGy : Float = 0.toFloat()

    // graphic size of the ball

    var picHeight: Int = 0
    var picWidth : Int = 0

    var icon:Bitmap ?= null

    // window size

    var Windowwidth : Int = 0
    var Windowheight : Int = 0

    // is touching the edge ?

    var noBorderX = false
    var noBorderY = false

    var vibratorService : Vibrator ?= null
    var thread : BolaDeMetal.DrawThread?= null



    init {
        holder.addCallback(this)
        //create a thread
        thread = BolaDeMetal.DrawThread(holder, this)
        // get references and sizes of the objects
        val display: Display = (getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        val size:Point = Point()
        display.getSize(size)
        Windowwidth = size.x
        Windowheight = size.y
        icon = BitmapFactory.decodeResource(resources, R.drawable.ball)
        picHeight = icon!!.height
        picWidth = icon!!.width
        vibratorService = (getContext().getSystemService(Service.VIBRATOR_SERVICE)) as Vibrator
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        thread!!.setRunning(true)
        thread!!.start()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if (canvas != null){
            canvas.drawColor(0xFFAAAAA)
            icon?.let { canvas.drawBitmap(it,cx,cy,null) }
            //canvas.drawBitmap(icon,cx,cy,null)
        }
    }

    override public fun onDraw(canvas: Canvas?) {

        if (canvas != null){
            canvas.drawColor(0xFFAAAAA)
            icon?.let { canvas.drawBitmap(it,cx,cy,null) }
            //canvas.drawBitmap(icon,cx,cy,null)
        }
    }

    fun updateMe(inx : Float , iny : Float){
        lastGx += inx
        lastGy += iny

        cx += lastGx
        cy += lastGy

        if(cx > (Windowwidth - picWidth)){
            cx = (Windowwidth - picWidth).toFloat()
            lastGx = 0F
            if (noBorderX){
                vibratorService!!.vibrate(100)
                noBorderX = false
            }
        }
        else if(cx < (0)){
            cx = 0F
            lastGx = 0F
            if(noBorderX){
                vibratorService!!.vibrate(100)
                noBorderX = false
            }
        }
        else{ noBorderX = true }

        if (cy > (Windowheight - picHeight)){
            cy = (Windowheight - picHeight).toFloat()
            lastGy = 0F
            if (noBorderY){
                vibratorService!!.vibrate(100)
                noBorderY = false
            }
        }

        else if(cy < (0)){
            cy = 0F
            lastGy = 0F
            if (noBorderY){
                vibratorService!!.vibrate(100)
                noBorderY= false
            }
        }
        else{ noBorderY = true }

        invalidate()

    }
}

//package com.example.helloandroidstudio
//
//import androidx.appcompat.app.AppCompatActivity
////import android.os.Bundle
//import android.app.Service
//import android.bluetooth.BluetoothClass
//import android.content.Context
//import android.content.pm.ActivityInfo
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.Canvas
//import android.graphics.Point
//import android.hardware.Sensor
//import android.hardware.SensorEvent
//import android.hardware.SensorEventListener
//import android.hardware.SensorManager
//import android.os.Build
////import android.support.v7.app.AppCompatActivity
//import android.os.Bundle
//import android.os.Vibrator
//import android.view.*
//
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.ActivityInfo;
//import android.content.res.Configuration;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.drawable.ShapeDrawable;
//import android.hardware.Sensor;
//import android.hardware.SensorEvent;
//import android.hardware.SensorEventListener;
//import android.hardware.SensorManager;
//import android.os.Bundle;
//import android.view.Display;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
////class BolaDeMetal : AppCompatActivity(), SensorEventListener {
//
//class BolaDeMetal : AppCompatActivity() , SensorEventListener{
//
////        CustomDrawableView mCustomDrawableView = null;
////        ShapeDrawable mDrawable = new ShapeDrawable();
////        public float xPosition, xAcceleration, xVelocity = 0.0f;
////        public float yPosition, yAcceleration, yVelocity = 0.0f;
////        public float xmax, ymax;
////        private Bitmap mBitmap;
////        private Bitmap mWood;
////        private SensorManager sensorManager = null;
////        public float frameTime = 0.666f;
//    lateinit var mCustomDrawableView:CustomDrawableView // = null;
//    var mDrawable:ShapeDrawable = ShapeDrawable();
//    var xPosition= 0.0f;
//    var xAcceleration= 0.0f;
//    var xVelocity = 0.0f;
//    var yPosition= 0.0f;
//    var yAcceleration= 0.0f;
//    var yVelocity = 0.0f;
//
//    var xmax = 0//.0f;
//    var ymax = 0//.0f;
//
//    private lateinit var mBitmap:Bitmap;
//    private lateinit var mWood:Bitmap;
//    private lateinit var sensorManager:SensorManager;
//    public var frameTime = 0.666f;
//
//    //TYPE_ACCELEROMETER
//        /** Called when the activity is first created. */
//        @Override
//        override fun onCreate(savedInstanceState:Bundle) {
//
//            super.onCreate(savedInstanceState);
//
//            // Set FullScreen & portrait
//            requestWindowFeature(Window.FEATURE_NO_TITLE);
//            getWindow().setFlags(
//                WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN
//            );
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//
//            // Get a reference to a SensorManager
//            sensorManager =  getSystemService (SENSOR_SERVICE) as SensorManager;
//            sensorManager.registerListener(
//                this,
//                //sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
//                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
//                SensorManager.SENSOR_DELAY_GAME
//            );
//
//            mCustomDrawableView = CustomDrawableView (this);
//            setContentView(mCustomDrawableView);
//            // setContentView(R.layout.main);
//
//            // Calculate Boundry
//            var display:Display = getWindowManager().getDefaultDisplay();
//            xmax = display.getWidth() - 50;
//            ymax = display.getHeight() - 50;
//        }
//
//        // This method will update the UI on new sensor events
//        //@Override
//        override fun onSensorChanged(sensorEvent:SensorEvent) {
//            {
//                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
//                    // Set sensor values as acceleration
//                    yAcceleration = sensorEvent.values[1];
//                    xAcceleration = sensorEvent.values[2];
//                    updateBall();
//                }
//            }
//        }
//
//    private fun updateBall() {
//
//        // Calculate new speed
//        xVelocity += (xAcceleration * frameTime);
//        yVelocity += (yAcceleration * frameTime);
//
//        // Calc distance travelled in that time
//        float xS =(xVelocity / 2) * frameTime;
//        float yS =(yVelocity / 2) * frameTime;
//
//        // Add to position negative due to sensor
//        // readings being opposite to what we want!
//        xPosition -= xS;
//        yPosition -= yS;
//
//        if (xPosition > xmax) {
//            xPosition = xmax;
//        } else if (xPosition < 0) {
//            xPosition = 0;
//        }
//        if (yPosition > ymax) {
//            yPosition = ymax;
//        } else if (yPosition < 0) {
//            yPosition = 0;
//        }
//    }
//
//    // I've chosen to not implement this method
//    public fun onAccuracyChanged(Sensor arg0, int arg1) {
//        // TODO Auto-generated method stub
//    }
//
//    @Override
//    protected fun onResume() {
//        super.onResume();
//        sensorManager.registerListener(
//            this,
//            sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
//            SensorManager.SENSOR_DELAY_GAME
//        );
//    }
//}
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        setContentView(R.layout.activity_bola_de_metal)
////    }
////}
////private var mSensorManager : SensorManager ?= null
////private var mAccelerometer : Sensor ?= null
////var ground : GroundView ?= null
////    private lateinit var mSensorManager : SensorManager// ?= null
////    private lateinit var mAccelerometer : Sensor// ?= null
////    private lateinit var ground : GroundView //?= null
////
////
////override fun onCreate(savedInstanceState: Bundle?) {
////    requestWindowFeature(Window.FEATURE_NO_TITLE)
////    super.onCreate(savedInstanceState)
////    // get reference of the service
////    mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
////    // focus in accelerometer
////    mAccelerometer = mSensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
////    // setup the window
////    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
////
////    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
////        WindowManager.LayoutParams.FLAG_FULLSCREEN)
////
////    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
////        window.decorView.systemUiVisibility =   View.SYSTEM_UI_FLAG_LAYOUT_STABLE
////        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
////        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
////        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
////        View.SYSTEM_UI_FLAG_FULLSCREEN
////        View.SYSTEM_UI_FLAG_IMMERSIVE
////    }
////
////    // set the view
////    ground = GroundView(this)
////    setContentView(ground)
////}
////
////
////override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
////}
////
////override fun onSensorChanged(event: SensorEvent?) {
////    if (event != null) {
////        ground!!.updateMe(event.values[1] , event.values[0])
////    }
////}
////
////override fun onResume() {
////    super.onResume()
////    mSensorManager!!.registerListener(this,mAccelerometer,
////        SensorManager.SENSOR_DELAY_GAME)
////}
////
////override fun onPause() {
////    super.onPause()
////    mSensorManager!!.unregisterListener(this)
////}
////
////class DrawThread (surfaceHolder: SurfaceHolder , panel : GroundView) : Thread() {
////    private var surfaceHolder :SurfaceHolder ?= null
////    private var panel : GroundView ?= null
////    private var run = false
////
////    init {
////        this.surfaceHolder = surfaceHolder
////        this.panel = panel
////    }
////
////    fun setRunning(run : Boolean){
////        this.run = run
////    }
////
////    override fun run() {
////        var c: Canvas ?= null
////        while (run){
////            c = null
////            try {
////                c = surfaceHolder!!.lockCanvas(null)
////                synchronized(surfaceHolder!!){
////                    panel!!.draw(c)
////                }
////            }finally {
////                if (c!= null){
////                    surfaceHolder!!.unlockCanvasAndPost(c)
////                }
////            }
////        }
////    }
////
////}
////
////}
////
////
////class GroundView(context: Context?) : SurfaceView(context), SurfaceHolder.Callback{
////
////    // ball coordinates
////    var cx : Float = 10.toFloat()
////    var cy : Float = 10.toFloat()
////
////    // last position increment
////
////    var lastGx : Float = 0.toFloat()
////    var lastGy : Float = 0.toFloat()
////
////    // graphic size of the ball
////
////    var picHeight: Int = 0
////    var picWidth : Int = 0
////
////    var icon:Bitmap ?= null
////
////    // window size
////
////    var Windowwidth : Int = 0
////    var Windowheight : Int = 0
////
////    // is touching the edge ?
////
////    var noBorderX = false
////    var noBorderY = false
////
////    var vibratorService : Vibrator ?= null
////    var thread : BolaDeMetal.DrawThread?= null
////
////
////
////    init {
////        holder.addCallback(this)
////        //create a thread
////        thread = BolaDeMetal.DrawThread(holder, this)
////        // get references and sizes of the objects
////        val display: Display = (getContext().getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
////        val size:Point = Point()
////        display.getSize(size)
////        Windowwidth = size.x
////        Windowheight = size.y
////        icon = BitmapFactory.decodeResource(resources,R.drawable.ball)
////        picHeight = icon!!.height
////        picWidth = icon!!.width
////        vibratorService = (getContext().getSystemService(Service.VIBRATOR_SERVICE)) as Vibrator
////    }
////
////    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
////    }
////
////    override fun surfaceDestroyed(holder: SurfaceHolder?) {
////    }
////
////    override fun surfaceCreated(holder: SurfaceHolder?) {
////        thread!!.setRunning(true)
////        thread!!.start()
////    }
////
////    override fun draw(canvas: Canvas?) {
////        super.draw(canvas)
////        if (canvas != null){
////            canvas.drawColor(0xFFAAAAA)
////            canvas.drawBitmap(icon as Bitmap,cx,cy,null)
////        }
////    }
////
////    override public fun onDraw(canvas: Canvas?) {
////
////        if (canvas != null){
////            canvas.drawColor(0xFFAAAAA)
////            canvas.drawBitmap(icon as Bitmap,cx,cy,null)
////        }
////    }
////
////    fun updateMe(inx : Float , iny : Float){
////        lastGx += inx
////        lastGy += iny
////
////        cx += lastGx
////        cy += lastGy
////
////        if(cx > (Windowwidth - picWidth)){
////            cx = (Windowwidth - picWidth).toFloat()
////            lastGx = 0F
////            if (noBorderX){
////                vibratorService!!.vibrate(100)
////                noBorderX = false
////            }
////        }
////        else if(cx < (0)){
////            cx = 0F
////            lastGx = 0F
////            if(noBorderX){
////                vibratorService!!.vibrate(100)
////                noBorderX = false
////            }
////        }
////        else{ noBorderX = true }
////
////        if (cy > (Windowheight - picHeight)){
////            cy = (Windowheight - picHeight).toFloat()
////            lastGy = 0F
////            if (noBorderY){
////                vibratorService!!.vibrate(100)
////                noBorderY = false
////            }
////        }
////
////        else if(cy < (0)){
////            cy = 0F
////            lastGy = 0F
////            if (noBorderY){
////                vibratorService!!.vibrate(100)
////                noBorderY= false
////            }
////        }
////        else{ noBorderY = true }
////
////        invalidate()
////
////    }
////}