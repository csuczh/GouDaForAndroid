package com.dg.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dg.app.R;

/**
 * 滑动按钮
 * 
 * @author Chaos(Thanks chenwen3k to help solve a serious problem)
 * @date 2013-05-25
 */
public class SwitchView extends FrameLayout {
	/**
	 * LeftSwitch
	 */
	private static final String TEXT_LEFT_SWITCH = "场景";
	/**
	 * RightSwitch
	 */
	private static final String TEXT_RIGHT_SWITCH = "发现";
	/**
	 * TYPE - LEFT
	 */
	public static final int TYPE_LEFT = 0;
	/**
	 * TYPE - Right
	 */
	public static final int TYPE_RIGHT = 1;
	/**
	 * The best width
	 */
	private static final int MAX_WIDTH = 300;
	/**
	 * The best height
	 */
	private static final int MAX_HEIGHT = 80;
	/**
	 * In order to trick your eyes
	 */
	private static final int FRAMES = 15;
	/**
	 * Used to describe the degree of shaking
	 */
	private static final int ARGUMENT_SHAKE = 2;
	/**
	 * The offset for the upper view
	 */
	private int mOffsetX = 0;
	/**
	 * Total width of the floating layer (but I think that may not be required)
	 */
	private int mFloatingLayerWidth;
	/**
	 * The total width of the view (but I think that may not be required)
	 */
	private int mTotalWidth;
	/**
	 * Description of the currently checked type
	 */
	private int mNowType = TYPE_LEFT;
	/**
	 * In order to listen for checked events
	 */
	private OnSwitchListener mSwitchListener;
	/**
	 * Necessary object
	 */
	private TextView mLeftSwitch;
	private TextView mRightSwitch;
	private ImageView mFloatingLayer;
	private LinearLayout mSwitchParent;
	/**
	 * Touchevent interceptor
	 */
	private GestureDetector mDetector = null;
	/**
	 * The click switch event flag
	 */
	private boolean isOpenClick = true;

	public SwitchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public SwitchView(Context context) {
		super(context);
		init();
	}

	private void init() {
		// build parent
		mSwitchParent = new LinearLayout(getContext());
		// Ready to work
		mSwitchParent.setOrientation(LinearLayout.HORIZONTAL);
		setBackgroundResource(R.drawable.usage_list_dark);
		// step.1: add floating layer in bottom(maybe called 'sediment layer')
		mFloatingLayer = new ImageView(getContext());
		mFloatingLayer.setBackgroundResource(R.drawable.usage_list_green);
		LayoutParams flowLayoutParams = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		addView(mFloatingLayer, flowLayoutParams);
		// step.2: init children
		mLeftSwitch = new TextView(getContext());
		mRightSwitch = new TextView(getContext());
		mLeftSwitch.setTextSize((float)20.0);
		mRightSwitch.setTextSize((float)20.0);

		// step.3: load res for the textView
		mLeftSwitch.setText(TEXT_LEFT_SWITCH);
		mRightSwitch.setText(TEXT_RIGHT_SWITCH);
		// step.4: whitewash and relocation the text
		mLeftSwitch.setBackgroundColor(Color.TRANSPARENT);
		mRightSwitch.setBackgroundColor(Color.TRANSPARENT);
		mLeftSwitch.setTextColor(Color.BLACK);
		mRightSwitch.setTextColor(Color.BLACK);
		mLeftSwitch.setGravity(Gravity.CENTER);
		mRightSwitch.setGravity(Gravity.CENTER);
		// step.5: build children LayoutParams
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		lp.weight = 1;
		mLeftSwitch.setLayoutParams(lp);
		mRightSwitch.setLayoutParams(lp);
		// step.6: add children
		mSwitchParent.addView(mLeftSwitch);
		mSwitchParent.addView(mRightSwitch);
		// step.7 : setup parent
		addView(mSwitchParent, LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// step.8 : handle flow
		mDetector = new GestureDetector(getContext(), new FlowGesture());

	}

	/**
	 * Register a callback to be invoked when this view is checked.
	 * 
	 * @param sl
	 *            The callback that will run
	 */
	public void setOnSwitchListener(OnSwitchListener sl) {
		this.mSwitchListener = sl;
	}

	/**
	 * Change the status of the current(will Slide)
	 */
	public void switching() {
		int type = getType();
		if (type == TYPE_LEFT) {
			sliding(TYPE_RIGHT);
		} else {
			sliding(TYPE_LEFT);
		}
	}

	/**
	 * Change the status of the current(maybe Slide)
	 * 
	 * @param type
	 * @see #TYPE_LEFT
	 * @see #TYPE_RIGHT
	 */
	public void switching(int type) {
		sliding(type);
	}

	/**
	 * Change the status of the current(maybe Slide)
	 * 
	 * @param isCheckedLeft
	 */
	public void switching(boolean isCheckedLeft) {
		System.out.println(isCheckedLeft);
		sliding(isCheckedLeft ? TYPE_LEFT : TYPE_RIGHT);
	}

	private int getType() {
		if (mOffsetX == 0) {
			return TYPE_LEFT;
		} else {
			return TYPE_RIGHT;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int wmode = MeasureSpec.getMode(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int hmode = MeasureSpec.getMode(heightMeasureSpec);

		width = getBestWidth(width);
		height = getBestHeight(height);

		mTotalWidth = width;

		int wSpec = MeasureSpec.makeMeasureSpec(width, wmode);
		int hSpec = MeasureSpec.makeMeasureSpec(height, hmode);

		mFloatingLayerWidth = getFlowWidth(width);

		mFloatingLayer.measure(
				MeasureSpec.makeMeasureSpec(mFloatingLayerWidth, wmode), hSpec);
		mSwitchParent.measure(wSpec, hSpec);
		setMeasuredDimension(wSpec, hSpec);
	}

	private int getBestWidth(int width) {
		return width > MAX_WIDTH ? MAX_WIDTH : width;
	}

	private int getBestHeight(int height) {
		return height > MAX_HEIGHT ? MAX_HEIGHT : height;
	}

	private int getFlowWidth(int total) {
		// expression (can make all the changes)
		return total / 2 + total / 15;
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		setup(mFloatingLayer, mOffsetX);
	}

	private void setup(View child, int offset) {
		child.layout(offset, 0, offset + child.getMeasuredWidth(),
				child.getMeasuredHeight());
	}

	public void closeClickEvent() {
		isOpenClick = false;
	}

	public void openClickEvent() {
		isOpenClick = true;
	}

	private void sliding(int type) {
		final int nowPosX = mOffsetX;
		final int targetX = getX(type);

		if (nowPosX == targetX) {
			// no meaning
			return;
		}

		final int distance = targetX - nowPosX;

		final FakeAnimation fake = new FakeAnimation(distance, 150);
		new Handler().post(new Runnable() {
			@Override
			public void run() {
				if (fake.isEnd()) {
					mOffsetX = targetX;
					requestLayout();
				} else {
					mOffsetX = fake.getValue() + mOffsetX;
					requestLayout();
					postDelayed(this, FRAMES);
				}
			}
		});

		// set current type
		mNowType = type;
		performOnCheck(mNowType);
	}

	private int getX(int type) {
		if (type == TYPE_LEFT) {
			return 0;
		} else if (type == TYPE_RIGHT) {
			return mTotalWidth - mFloatingLayerWidth;
		} else {
			return 0;
		}
	}

	/**
	 * Call this view's OnSwitchListener
	 */
	private void performOnCheck(int type) {
		if (mSwitchListener == null) {
			return;
		}
		if (type == TYPE_LEFT) {
			mSwitchListener.onCheck(this, true, false);
		} else if (type == TYPE_RIGHT) {
			mSwitchListener.onCheck(this, false, true);
		} else {
			// i don't know really will run here?
			mSwitchListener.onCheck(this, false, false);
		}
	}

	public TextView getLeftView() {
		return mLeftSwitch;
	}

	public TextView getRightView() {
		return mRightSwitch;
	}

	private int getFlowXWithLimit(double nowX, double flowX) {
		if (nowX < 0) {
			shake(TYPE_LEFT);
			return 0;
		} else if (nowX > (mTotalWidth - mFloatingLayerWidth)) {
			shake(TYPE_RIGHT);
			return (mTotalWidth - mFloatingLayerWidth);
		}
		return (int) flowX;
	}

	private void shake(int type) {
		int amplitude;
		if (type == TYPE_LEFT) {
			amplitude = ARGUMENT_SHAKE;
		} else if (type == TYPE_RIGHT) {
			amplitude = -ARGUMENT_SHAKE;
		} else {
			amplitude = 0;
		}
		mOffsetX = mOffsetX + amplitude;
		requestLayout();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		super.onTouchEvent(event);
		final int x = (int) event.getX();

		if (event.getAction() == MotionEvent.ACTION_UP) {
			// if the action equal UP, must reset the flow view;
			int type = x > mTotalWidth / 2 ? TYPE_RIGHT : TYPE_LEFT;
			sliding(type);
		}
		return mDetector.onTouchEvent(event);
	}

	class FlowGesture extends SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// common scroll
			mOffsetX = getFlowXWithLimit(mOffsetX, mOffsetX - distanceX);
			requestLayout();
			return true;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			final int x = (int) e.getX();
			final int y = (int) e.getY();

			Rect hitRect = new Rect();
			mFloatingLayer.getHitRect(hitRect);
			if (!hitRect.contains(x, y)) {
				if (isOpenClick) {
					switching();
				}
				return false;
			}
			return true;
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			if (isOpenClick) {
				switching();
			}
			return super.onSingleTapUp(e);
		}

	}

	/**
	 * ha, cheat you!
	 * 
	 * @author Chaos
	 */
	private static class FakeAnimation {

		private int times;
		private int nowTimes;
		private int ceil;

		FakeAnimation(int distance, long time) {
			times = (int) (time / FRAMES);
			ceil = distance / times;
			nowTimes = 0;
		}

		public boolean isEnd() {
			return nowTimes == times;
		}

		public int getValue() {
			nowTimes++;
			return ceil;
		}
	}

	/**
	 * Interface definition for a callback to be invoked when a view is checked.
	 */
	public static interface OnSwitchListener {
		/**
		 * Called when a view has been checked.
		 * 
		 * @param sv
		 *            The view that was checked.
		 * @param checkLeft
		 *            will be true if the leftSwitch was checked , otherwise
		 *            return false.
		 * @param checkRight
		 *            will be true if the rightSwitch was checked , otherwise
		 *            return false.
		 */
		public void onCheck(SwitchView sv, boolean checkLeft, boolean checkRight);
	}
}
