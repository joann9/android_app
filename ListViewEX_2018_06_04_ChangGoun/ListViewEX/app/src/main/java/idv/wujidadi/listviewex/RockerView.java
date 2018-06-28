package idv.wujidadi.listviewex;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jinkia on 2016/9/30.
 * 搖桿控件
 */
public class RockerView extends View {
    private static final String TAG = "RockerView";

    private static final int DEFAULT_SIZE = 400;
    private static final float DEFAULT_ROCKER_SCALE = 0.5f;//預設半徑為背景的1/2

    private Paint mAreaBackgroundPaint;
    private Paint mRockerPaint;

    private Point mRockerPosition;
    private Point mCenterPoint;

    private int mAreaRadius;
    private float mRockerScale;

    private int mRockerRadius;

    private CallBackMode mCallBackMode = CallBackMode.CALL_BACK_MODE_MOVE;
    private OnAngleChangeListener mOnAngleChangeListener;
    private OnShakeListener mOnShakeListener;
    private OnDistanceLevelListener mOnDistanceLevelListener;

    private DirectionMode mDirectionMode;
    private Direction tempDirection = Direction.DIRECTION_CENTER;

    private float lastDistance = 0;
    private boolean hasCall = false;
    private float baseDistance = 0;
    private int mDistanceLevel = 10;//分成10份


    // 角度
    private static final double ANGLE_0 = 0;
    private static final double ANGLE_360 = 360;
    // 360°水平方向平分2份的邊緣角度
    private static final double ANGLE_HORIZONTAL_2D_OF_0P = 90;
    private static final double ANGLE_HORIZONTAL_2D_OF_1P = 270;
    // 360°垂直方向平分2份的邊緣角度
    private static final double ANGLE_VERTICAL_2D_OF_0P = 0;
    private static final double ANGLE_VERTICAL_2D_OF_1P = 180;
    // 360°平分4份的邊緣角度
    private static final double ANGLE_4D_OF_0P = 0;
    private static final double ANGLE_4D_OF_1P = 90;
    private static final double ANGLE_4D_OF_2P = 180;
    private static final double ANGLE_4D_OF_3P = 270;
    // 360°平分4份的邊緣角度(旋轉45度)
    private static final double ANGLE_ROTATE45_4D_OF_0P = 45;
    private static final double ANGLE_ROTATE45_4D_OF_1P = 135;
    private static final double ANGLE_ROTATE45_4D_OF_2P = 225;
    private static final double ANGLE_ROTATE45_4D_OF_3P = 315;

    // 360°平分8份的邊緣角度
    private static final double ANGLE_8D_OF_0P = 22.5;
    private static final double ANGLE_8D_OF_1P = 67.5;
    private static final double ANGLE_8D_OF_2P = 112.5;
    private static final double ANGLE_8D_OF_3P = 157.5;
    private static final double ANGLE_8D_OF_4P = 202.5;
    private static final double ANGLE_8D_OF_5P = 247.5;
    private static final double ANGLE_8D_OF_6P = 292.5;
    private static final double ANGLE_8D_OF_7P = 337.5;

    // 搖桿可移動區域背景
    private static final int AREA_BACKGROUND_MODE_PIC = 0;
    private static final int AREA_BACKGROUND_MODE_COLOR = 1;
    private static final int AREA_BACKGROUND_MODE_XML = 2;
    private static final int AREA_BACKGROUND_MODE_DEFAULT = 3;
    private int mAreaBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT;
    private Bitmap mAreaBitmap;
    private int mAreaColor;
    // 搖桿背景
    private static final int ROCKER_BACKGROUND_MODE_PIC = 4;
    private static final int ROCKER_BACKGROUND_MODE_COLOR = 5;
    private static final int ROCKER_BACKGROUND_MODE_XML = 6;
    private static final int ROCKER_BACKGROUND_MODE_DEFAULT = 7;
    private int mRockerBackgroundMode = ROCKER_BACKGROUND_MODE_DEFAULT;
    private Bitmap mRockerBitmap;
    private int mRockerColor;


    public RockerView(Context context, AttributeSet attrs) {
        super(context, attrs);

        // 取得自訂屬性
        initAttribute(context, attrs);

        if (isInEditMode()) {
        }

        // 移動區域畫筆
        mAreaBackgroundPaint = new Paint();
        mAreaBackgroundPaint.setAntiAlias(true);

        // 搖桿畫筆
        mRockerPaint = new Paint();
        mRockerPaint.setAntiAlias(true);

        // 中心點
        mCenterPoint = new Point();
        // 搖桿位置
        mRockerPosition = new Point();
    }

    /**
     * 取得屬性
     *
     * @param context context
     * @param attrs   attrs
     */
    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RockerView);

        // 可移動區域背景
        Drawable areaBackground = typedArray.getDrawable(R.styleable.RockerView_areaBackground);
        if (null != areaBackground) {
            // 設置背景
            if (areaBackground instanceof BitmapDrawable) {
                // 設置一張圖片
                mAreaBitmap = ((BitmapDrawable) areaBackground).getBitmap();
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_PIC;
            } else if (areaBackground instanceof GradientDrawable) {
                // XML
                mAreaBitmap = drawable2Bitmap(areaBackground);
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_XML;
            } else if (areaBackground instanceof ColorDrawable) {
                // 色值
                mAreaColor = ((ColorDrawable) areaBackground).getColor();
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_COLOR;
            } else {
                // 其他形式
                mAreaBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT;
            }
        } else {
            // 沒有設置背景
            mAreaBackgroundMode = AREA_BACKGROUND_MODE_DEFAULT;
        }
        // 搖桿背景
        Drawable rockerBackground = typedArray.getDrawable(R.styleable.RockerView_rockerBackground);
        if (null != rockerBackground) {
            // 設置搖桿背景
            if (rockerBackground instanceof BitmapDrawable) {
                // 圖片
                mRockerBitmap = ((BitmapDrawable) rockerBackground).getBitmap();
                mRockerBackgroundMode = ROCKER_BACKGROUND_MODE_PIC;
            } else if (rockerBackground instanceof GradientDrawable) {
                // XML
                mRockerBitmap = drawable2Bitmap(rockerBackground);
                mRockerBackgroundMode = ROCKER_BACKGROUND_MODE_XML;
            } else if (rockerBackground instanceof ColorDrawable) {
                // 色值
                mRockerColor = ((ColorDrawable) rockerBackground).getColor();
                mRockerBackgroundMode = ROCKER_BACKGROUND_MODE_COLOR;
            } else {
                // 其他形式
                mRockerBackgroundMode = ROCKER_BACKGROUND_MODE_DEFAULT;
            }
        } else {
            // 沒有設置搖桿背景
            mRockerBackgroundMode = ROCKER_BACKGROUND_MODE_DEFAULT;
        }

        // 搖桿半径
        mRockerScale = typedArray.getFloat(R.styleable.RockerView_rockerScale, DEFAULT_ROCKER_SCALE);
        // 距離級別
        mDistanceLevel = typedArray.getInt(R.styleable.RockerView_rockerSpeedLevel, 10);
        // 模式
        mCallBackMode = getCallBackMode(typedArray.getInt(R.styleable.RockerView_rockerCallBackMode, 0));
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth, measureHeight;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            // 具體的值和match_parent
            measureWidth = widthSize;
        } else {
            // wrap_content
            measureWidth = DEFAULT_SIZE;
        }
        if (heightMode == MeasureSpec.EXACTLY) {
            measureHeight = heightSize;
        } else {
            measureHeight = DEFAULT_SIZE;
        }
        setMeasuredDimension(measureWidth, measureHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();

        int cx = measuredWidth / 2;
        int cy = measuredHeight / 2;

        // 中心點
        mCenterPoint.set(cx, cy);
        // 可移動區域的半徑
        mAreaRadius = (measuredWidth <= measuredHeight) ? (int) (cx / (mRockerScale + 1)) : (int) (cy / (mRockerScale + 1));
        mRockerRadius = (int) (mAreaRadius * mRockerScale);
        // 搖桿位置
        if (0 == mRockerPosition.x || 0 == mRockerPosition.y) {
            mRockerPosition.set(mCenterPoint.x, mCenterPoint.y);
        }

        // 畫可移動區域
        if (AREA_BACKGROUND_MODE_PIC == mAreaBackgroundMode || AREA_BACKGROUND_MODE_XML == mAreaBackgroundMode) {
            // 圖片
            Rect src = new Rect(0, 0, mAreaBitmap.getWidth(), mAreaBitmap.getHeight());
            Rect dst = new Rect(mCenterPoint.x - mAreaRadius, mCenterPoint.y - mAreaRadius, mCenterPoint.x + mAreaRadius, mCenterPoint.y + mAreaRadius);
            canvas.drawBitmap(mAreaBitmap, src, dst, mAreaBackgroundPaint);
        } else if (AREA_BACKGROUND_MODE_COLOR == mAreaBackgroundMode) {
            // 色值
            mAreaBackgroundPaint.setColor(mAreaColor);
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mAreaRadius, mAreaBackgroundPaint);
        } else {
            // 其他或者未設置
            mAreaBackgroundPaint.setColor(Color.GRAY);
            canvas.drawCircle(mCenterPoint.x, mCenterPoint.y, mAreaRadius, mAreaBackgroundPaint);
        }

        // 畫搖桿
        if (ROCKER_BACKGROUND_MODE_PIC == mRockerBackgroundMode || ROCKER_BACKGROUND_MODE_XML == mRockerBackgroundMode) {
            // 圖片
            Rect src = new Rect(0, 0, mRockerBitmap.getWidth(), mRockerBitmap.getHeight());
            Rect dst = new Rect(mRockerPosition.x - mRockerRadius, mRockerPosition.y - mRockerRadius, mRockerPosition.x + mRockerRadius, mRockerPosition.y + mRockerRadius);
            canvas.drawBitmap(mRockerBitmap, src, dst, mRockerPaint);
        } else if (ROCKER_BACKGROUND_MODE_COLOR == mRockerBackgroundMode) {
            // 色值
            mRockerPaint.setColor(mRockerColor);
            canvas.drawCircle(mRockerPosition.x, mRockerPosition.y, mRockerRadius, mRockerPaint);
        } else {
            // 其他或者未設置
            mRockerPaint.setColor(Color.RED);
            canvas.drawCircle(mRockerPosition.x, mRockerPosition.y, mRockerRadius, mRockerPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:// 按下
                // 回調 開始
                callBackStart();
            case MotionEvent.ACTION_MOVE:// 移動
                float moveX = event.getX();
                float moveY = event.getY();
                baseDistance = mAreaRadius+2;
                Log.d("baseDistance",baseDistance+"");
                mRockerPosition = getRockerPositionPoint(mCenterPoint, new Point((int) moveX, (int) moveY), mAreaRadius + mRockerRadius, mRockerRadius);
                moveRocker(mRockerPosition.x, mRockerPosition.y);
                break;
            case MotionEvent.ACTION_UP:// 抬起
            case MotionEvent.ACTION_CANCEL:// 移出區域
                // 回調 结束
                callBackFinish();
                if (mOnShakeListener != null) {
                    mOnShakeListener.direction(Direction.DIRECTION_CENTER);
                }
                float upX = event.getX();
                float upY = event.getY();
                moveRocker(mCenterPoint.x, mCenterPoint.y);
                break;
        }
        return true;
    }

    /**
     * 獲取搖桿實際要顯示的位置（點）
     *
     * @param centerPoint  中心點
     * @param touchPoint   觸摸點
     * @param regionRadius 搖桿可活動區域半徑
     * @param rockerRadius 搖桿半徑
     * @return 搖桿實際顯示的位置（點）
     */
    private Point getRockerPositionPoint(Point centerPoint, Point touchPoint, float regionRadius, float rockerRadius) {
        // 兩點在X軸的距離
        float lenX = (float) (touchPoint.x - centerPoint.x);
        // 兩點在Y軸距離
        float lenY = (float) (touchPoint.y - centerPoint.y);
        // 兩點距離
        float lenXY = (float) Math.sqrt((double) (lenX * lenX + lenY * lenY));
        // 計算弧度
        double radian = Math.acos(lenX / lenXY) * (touchPoint.y < centerPoint.y ? -1 : 1);
        // 計算角度
        double angle = radian2Angle(radian);

        if (lenXY + rockerRadius <= regionRadius) { // 觸摸位置在可活動範圍內
            // 回調 返回參數
            callBack(angle, (int) lenXY);
            return touchPoint;
        } else { // 觸摸位置在可活動範圍以外
            // 計算要顯示的位置
            int showPointX = (int) (centerPoint.x + (regionRadius - rockerRadius) * Math.cos(radian));
            int showPointY = (int) (centerPoint.y + (regionRadius - rockerRadius) * Math.sin(radian));

            callBack(angle, (int) Math.sqrt((showPointX - centerPoint.x) * (showPointX - centerPoint.x) + (showPointY - centerPoint.y) * (showPointY - centerPoint.y)));
            return new Point(showPointX, showPointY);
        }
    }

    /**
     * 移動搖桿到指定位置
     *
     * @param x x座標
     * @param y y座標
     */
    private void moveRocker(float x, float y) {
        mRockerPosition.set((int) x, (int) y);
        invalidate();
    }

    /**
     * 弧度轉角度
     *
     * @param radian 弧度
     * @return 角度[0, 360)
     */
    private double radian2Angle(double radian) {
        double tmp = Math.round(radian / Math.PI * 180);
        return tmp >= 0 ? tmp : 360 + tmp;
    }

    /**
     * Drawable 轉 Bitmap
     *
     * @param drawable Drawable
     * @return Bitmap
     */
    private Bitmap drawable2Bitmap(Drawable drawable) {
        // 取 drawable 的長寬
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        // 取 drawable 的顏色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        // 建立對應 bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 回調
     * 開始
     */
    private void callBackStart() {
        tempDirection = Direction.DIRECTION_CENTER;
        if (null != mOnAngleChangeListener) {
            mOnAngleChangeListener.onStart();
        }
        if (null != mOnShakeListener) {
            mOnShakeListener.onStart();
        }
    }

    /**
     * 回調
     * 返回參數
     *
     * @param angle 搖動角度
     */
    private void callBack(double angle, float distance) {
        Log.d("distance",distance+"");
        if (Math.abs(distance - lastDistance) >= (baseDistance / mDistanceLevel)) {
            lastDistance = distance;
            if (null != mOnDistanceLevelListener) {
                int level = (int) (distance/(baseDistance/mDistanceLevel));
                mOnDistanceLevelListener.onDistanceLevel(level);
            }
        }
        if (null != mOnAngleChangeListener) {
            mOnAngleChangeListener.angle(angle);
        }
        if (null != mOnShakeListener) {
            if (CallBackMode.CALL_BACK_MODE_MOVE == mCallBackMode) {
                switch (mDirectionMode) {
                    case DIRECTION_2_HORIZONTAL:// 左右方向
                        if (ANGLE_0 <= angle && ANGLE_HORIZONTAL_2D_OF_0P > angle || ANGLE_HORIZONTAL_2D_OF_1P <= angle && ANGLE_360 > angle) {
                            // 右
                            mOnShakeListener.direction(Direction.DIRECTION_RIGHT);
                        } else if (ANGLE_HORIZONTAL_2D_OF_0P <= angle && ANGLE_HORIZONTAL_2D_OF_1P > angle) {
                            // 左
                            mOnShakeListener.direction(Direction.DIRECTION_LEFT);
                        }
                        break;
                    case DIRECTION_2_VERTICAL:// 上下方向
                        if (ANGLE_VERTICAL_2D_OF_0P <= angle && ANGLE_VERTICAL_2D_OF_1P > angle) {
                            // 下
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN);
                        } else if (ANGLE_VERTICAL_2D_OF_1P <= angle && ANGLE_360 > angle) {
                            // 上
                            mOnShakeListener.direction(Direction.DIRECTION_UP);
                        }
                        break;
                    case DIRECTION_4_ROTATE_0:// 四個方向
                        if (ANGLE_4D_OF_0P <= angle && ANGLE_4D_OF_1P > angle) {
                            // 右下
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN_RIGHT);
                        } else if (ANGLE_4D_OF_1P <= angle && ANGLE_4D_OF_2P > angle) {
                            // 左下
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN_LEFT);
                        } else if (ANGLE_4D_OF_2P <= angle && ANGLE_4D_OF_3P > angle) {
                            // 左上
                            mOnShakeListener.direction(Direction.DIRECTION_UP_LEFT);
                        } else if (ANGLE_4D_OF_3P <= angle && ANGLE_360 > angle) {
                            // 右上
                            mOnShakeListener.direction(Direction.DIRECTION_UP_RIGHT);
                        }
                        break;
                    case DIRECTION_4_ROTATE_45:// 四個方向 旋轉45度
                        if (ANGLE_0 <= angle && ANGLE_ROTATE45_4D_OF_0P > angle || ANGLE_ROTATE45_4D_OF_3P <= angle && ANGLE_360 > angle) {
                            // 右
                            mOnShakeListener.direction(Direction.DIRECTION_RIGHT);
                        } else if (ANGLE_ROTATE45_4D_OF_0P <= angle && ANGLE_ROTATE45_4D_OF_1P > angle) {
                            // 下
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN);
                        } else if (ANGLE_ROTATE45_4D_OF_1P <= angle && ANGLE_ROTATE45_4D_OF_2P > angle) {
                            // 左
                            mOnShakeListener.direction(Direction.DIRECTION_LEFT);
                        } else if (ANGLE_ROTATE45_4D_OF_2P <= angle && ANGLE_ROTATE45_4D_OF_3P > angle) {
                            // 上
                            mOnShakeListener.direction(Direction.DIRECTION_UP);
                        }
                        break;
                    case DIRECTION_8:// 八個方向
                        if (ANGLE_0 <= angle && ANGLE_8D_OF_0P > angle || ANGLE_8D_OF_7P <= angle && ANGLE_360 > angle) {
                            // 右
                            mOnShakeListener.direction(Direction.DIRECTION_RIGHT);
                        } else if (ANGLE_8D_OF_0P <= angle && ANGLE_8D_OF_1P > angle) {
                            // 右下
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN_RIGHT);
                        } else if (ANGLE_8D_OF_1P <= angle && ANGLE_8D_OF_2P > angle) {
                            // 下
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN);
                        } else if (ANGLE_8D_OF_2P <= angle && ANGLE_8D_OF_3P > angle) {
                            // 左下
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN_LEFT);
                        } else if (ANGLE_8D_OF_3P <= angle && ANGLE_8D_OF_4P > angle) {
                            // 左
                            mOnShakeListener.direction(Direction.DIRECTION_LEFT);
                        } else if (ANGLE_8D_OF_4P <= angle && ANGLE_8D_OF_5P > angle) {
                            // 左上
                            mOnShakeListener.direction(Direction.DIRECTION_UP_LEFT);
                        } else if (ANGLE_8D_OF_5P <= angle && ANGLE_8D_OF_6P > angle) {
                            // 上
                            mOnShakeListener.direction(Direction.DIRECTION_UP);
                        } else if (ANGLE_8D_OF_6P <= angle && ANGLE_8D_OF_7P > angle) {
                            // 右上
                            mOnShakeListener.direction(Direction.DIRECTION_UP_RIGHT);
                        }
                        break;
                    default:
                        break;
                }
            } else if (CallBackMode.CALL_BACK_MODE_STATE_CHANGE == mCallBackMode) {
                switch (mDirectionMode) {
                    case DIRECTION_2_HORIZONTAL:// 左右方向
                        if ((ANGLE_0 <= angle && ANGLE_HORIZONTAL_2D_OF_0P > angle || ANGLE_HORIZONTAL_2D_OF_1P <= angle && ANGLE_360 > angle) && tempDirection != Direction.DIRECTION_RIGHT) {
                            // 右
                            tempDirection = Direction.DIRECTION_RIGHT;
                            mOnShakeListener.direction(Direction.DIRECTION_RIGHT);
                        } else if (ANGLE_HORIZONTAL_2D_OF_0P <= angle && ANGLE_HORIZONTAL_2D_OF_1P > angle && tempDirection != Direction.DIRECTION_LEFT) {
                            // 左
                            tempDirection = Direction.DIRECTION_LEFT;
                            mOnShakeListener.direction(Direction.DIRECTION_LEFT);
                        }
                        break;
                    case DIRECTION_2_VERTICAL:// 上下方向
                        if (ANGLE_VERTICAL_2D_OF_0P <= angle && ANGLE_VERTICAL_2D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN) {
                            // 下
                            tempDirection = Direction.DIRECTION_DOWN;
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN);
                        } else if (ANGLE_VERTICAL_2D_OF_1P <= angle && ANGLE_360 > angle && tempDirection != Direction.DIRECTION_UP) {
                            // 上
                            tempDirection = Direction.DIRECTION_UP;
                            mOnShakeListener.direction(Direction.DIRECTION_UP);
                        }
                        break;
                    case DIRECTION_4_ROTATE_0:// 四個方向
                        if (ANGLE_4D_OF_0P <= angle && ANGLE_4D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN_RIGHT) {
                            // 右下
                            tempDirection = Direction.DIRECTION_DOWN_RIGHT;
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN_RIGHT);
                        } else if (ANGLE_4D_OF_1P <= angle && ANGLE_4D_OF_2P > angle && tempDirection != Direction.DIRECTION_DOWN_LEFT) {
                            // 左下
                            tempDirection = Direction.DIRECTION_DOWN_LEFT;
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN_LEFT);
                        } else if (ANGLE_4D_OF_2P <= angle && ANGLE_4D_OF_3P > angle && tempDirection != Direction.DIRECTION_UP_LEFT) {
                            // 左上
                            tempDirection = Direction.DIRECTION_UP_LEFT;
                            mOnShakeListener.direction(Direction.DIRECTION_UP_LEFT);
                        } else if (ANGLE_4D_OF_3P <= angle && ANGLE_360 > angle && tempDirection != Direction.DIRECTION_UP_RIGHT) {
                            // 右上
                            tempDirection = Direction.DIRECTION_UP_RIGHT;
                            mOnShakeListener.direction(Direction.DIRECTION_UP_RIGHT);
                        }
                        break;
                    case DIRECTION_4_ROTATE_45:// 四個方向 旋轉45度
                        if ((ANGLE_0 <= angle && ANGLE_ROTATE45_4D_OF_0P > angle || ANGLE_ROTATE45_4D_OF_3P <= angle && ANGLE_360 > angle) && tempDirection != Direction.DIRECTION_RIGHT) {
                            // 右
                            tempDirection = Direction.DIRECTION_RIGHT;
                            mOnShakeListener.direction(Direction.DIRECTION_RIGHT);
                        } else if (ANGLE_ROTATE45_4D_OF_0P <= angle && ANGLE_ROTATE45_4D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN) {
                            // 下
                            tempDirection = Direction.DIRECTION_DOWN;
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN);
                        } else if (ANGLE_ROTATE45_4D_OF_1P <= angle && ANGLE_ROTATE45_4D_OF_2P > angle && tempDirection != Direction.DIRECTION_LEFT) {
                            // 左
                            tempDirection = Direction.DIRECTION_LEFT;
                            mOnShakeListener.direction(Direction.DIRECTION_LEFT);
                        } else if (ANGLE_ROTATE45_4D_OF_2P <= angle && ANGLE_ROTATE45_4D_OF_3P > angle && tempDirection != Direction.DIRECTION_UP) {
                            // 上
                            tempDirection = Direction.DIRECTION_UP;
                            mOnShakeListener.direction(Direction.DIRECTION_UP);
                        }
                        break;
                    case DIRECTION_8:// 八個方向
                        if ((ANGLE_0 <= angle && ANGLE_8D_OF_0P > angle || ANGLE_8D_OF_7P <= angle && ANGLE_360 > angle) && tempDirection != Direction.DIRECTION_RIGHT) {
                            // 右
                            tempDirection = Direction.DIRECTION_RIGHT;
                            mOnShakeListener.direction(Direction.DIRECTION_RIGHT);
                        } else if (ANGLE_8D_OF_0P <= angle && ANGLE_8D_OF_1P > angle && tempDirection != Direction.DIRECTION_DOWN_RIGHT) {
                            // 右下
                            tempDirection = Direction.DIRECTION_DOWN_RIGHT;
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN_RIGHT);
                        } else if (ANGLE_8D_OF_1P <= angle && ANGLE_8D_OF_2P > angle && tempDirection != Direction.DIRECTION_DOWN) {
                            // 下
                            tempDirection = Direction.DIRECTION_DOWN;
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN);
                        } else if (ANGLE_8D_OF_2P <= angle && ANGLE_8D_OF_3P > angle && tempDirection != Direction.DIRECTION_DOWN_LEFT) {
                            // 左下
                            tempDirection = Direction.DIRECTION_DOWN_LEFT;
                            mOnShakeListener.direction(Direction.DIRECTION_DOWN_LEFT);
                        } else if (ANGLE_8D_OF_3P <= angle && ANGLE_8D_OF_4P > angle && tempDirection != Direction.DIRECTION_LEFT) {
                            // 左
                            tempDirection = Direction.DIRECTION_LEFT;
                            mOnShakeListener.direction(Direction.DIRECTION_LEFT);
                        } else if (ANGLE_8D_OF_4P <= angle && ANGLE_8D_OF_5P > angle && tempDirection != Direction.DIRECTION_UP_LEFT) {
                            // 左上
                            tempDirection = Direction.DIRECTION_UP_LEFT;
                            mOnShakeListener.direction(Direction.DIRECTION_UP_LEFT);
                        } else if (ANGLE_8D_OF_5P <= angle && ANGLE_8D_OF_6P > angle && tempDirection != Direction.DIRECTION_UP) {
                            // 上
                            tempDirection = Direction.DIRECTION_UP;
                            mOnShakeListener.direction(Direction.DIRECTION_UP);
                        } else if (ANGLE_8D_OF_6P <= angle && ANGLE_8D_OF_7P > angle && tempDirection != Direction.DIRECTION_UP_RIGHT) {
                            // 右上
                            tempDirection = Direction.DIRECTION_UP_RIGHT;
                            mOnShakeListener.direction(Direction.DIRECTION_UP_RIGHT);
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 回調
     * 結束
     */
    private void callBackFinish() {
        tempDirection = Direction.DIRECTION_CENTER;
        if (null != mOnAngleChangeListener) {
            mOnAngleChangeListener.onFinish();
        }
        if (null != mOnShakeListener) {
            mOnShakeListener.onFinish();
        }
    }

    /**
     * 回調模式
     */
    public enum CallBackMode {
        // 有移動就立刻回調
        CALL_BACK_MODE_MOVE,
        // 只有狀態變化的時候才回調
        CALL_BACK_MODE_STATE_CHANGE,
        //只有狀態變化或者距離變化的時候才回調
        CALL_BACK_MODE_STATE_DISTANCE_CHANGE
    }

    /**
     * 設置回調模式
     *
     * @param mode 回調模式
     */
    public void setCallBackMode(CallBackMode mode) {
        mCallBackMode = mode;
    }

    /**
     * 搖桿支持幾個方向
     */
    public enum DirectionMode {
        DIRECTION_2_HORIZONTAL,// 橫向 左右兩個方向
        DIRECTION_2_VERTICAL, // 縱向 上下兩個方向
        DIRECTION_4_ROTATE_0, // 四個方向
        DIRECTION_4_ROTATE_45, // 四個方向 旋轉45度
        DIRECTION_8 // 八個方向
    }

    /**
     * 方向
     */
    public enum Direction {
        DIRECTION_LEFT, // 左
        DIRECTION_RIGHT, // 右
        DIRECTION_UP, // 上
        DIRECTION_DOWN, // 下
        DIRECTION_UP_LEFT, // 左上
        DIRECTION_UP_RIGHT, // 右上
        DIRECTION_DOWN_LEFT, // 左下
        DIRECTION_DOWN_RIGHT, // 右下
        DIRECTION_CENTER // 中間
    }

    /**
     * 添加搖桿搖動角度的監聽
     *
     * @param listener 回調接口
     */
    public void setOnAngleChangeListener(OnAngleChangeListener listener) {
        mOnAngleChangeListener = listener;
    }

    /**
     * 添加搖動的監聽
     *
     * @param directionMode 監聽的方向
     * @param listener      回調
     */
    public void setOnShakeListener(DirectionMode directionMode, OnShakeListener listener) {
        mDirectionMode = directionMode;
        mOnShakeListener = listener;
    }

    /**
     * 添加搖動的距離變化
     */
    public void setOnDistanceLevelListener(OnDistanceLevelListener listenr) {
        mOnDistanceLevelListener = listenr;
    }

    /**
     * 搖動方向監聽接口
     */
    public interface OnShakeListener {
        // 開始
        void onStart();

        /**
         * 搖動方向
         *
         * @param direction 方向
         */
        void direction(Direction direction);

        // 結束
        void onFinish();
    }

    /**
     * 搖動角度的監聽接口
     */
    public interface OnAngleChangeListener {
        // 開始
        void onStart();

        /**
         * 搖桿角度變化
         *
         * @param angle 角度[0,360)
         */
        void angle(double angle);

        // 結束
        void onFinish();
    }

    /**
     * 搖動距離
     */
    public interface OnDistanceLevelListener {

        void onDistanceLevel(int level);
    }

    private CallBackMode getCallBackMode(int mode) {
        switch (mode) {
            case 0:
                return CallBackMode.CALL_BACK_MODE_MOVE;
            case 1:
                return CallBackMode.CALL_BACK_MODE_STATE_CHANGE;
        }
        return mCallBackMode;
    }
}