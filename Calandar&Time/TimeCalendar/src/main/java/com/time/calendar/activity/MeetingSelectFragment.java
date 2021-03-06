package com.time.calendar.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.time.calendar.R;
import com.time.calendar.view.MyCalendarView;

import java.util.ArrayList;

public class MeetingSelectFragment extends Fragment implements
		OnItemClickListener
{

	private Context context;

	private TextView mTvSelect;

	private GridView mGvDuration;

	private GridView mGvRoom;

	private TextView mTvRoom;

	private TextView mTvSelelctRoom;

	private int mSelectRoomPosition = -1;

	private ArrayList<String> mRoomList;

	private ArrayList<Integer> mSelectTimePosition;

	private ArrayList<Integer> mFilterTimePosition;

	private int mUnselectColor;

	private Animation mAnimZoomIn;

	private MyCalendarView mMyCalendarView;

	private RoomAdapter mRoomAdapter;

	private DurationAdapter mDurationAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState)
	{
		context = getActivity();

		View view = inflater.inflate(R.layout.fragment_meeting_select_time,
				null);
		mGvRoom = (GridView) view.findViewById(R.id.grid_room);
		mGvDuration = (GridView) view.findViewById(R.id.grid_duration);
		mMyCalendarView = (MyCalendarView) view.findViewById(R.id.calendar);
		mTvSelect = (TextView) view.findViewById(R.id.tv_select);
		mTvRoom = (TextView) view.findViewById(R.id.tv_room);
		mTvSelelctRoom = (TextView) view.findViewById(R.id.tv_select_room);

		mRoomAdapter = new RoomAdapter();
		mDurationAdapter = new DurationAdapter();
		mGvRoom.setAdapter(mRoomAdapter);
		mGvDuration.setAdapter(mDurationAdapter);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		mSelectTimePosition = new ArrayList<Integer>();

		mGvDuration.setOnItemClickListener(this);
		mTvSelect.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				startActivityForResult(new Intent(context,
								SelectDateActivity.class),
						MeetingSettingActivity.REQ_SELECT_DATE);
			}
		});

		mUnselectColor = getResources().getColor(R.color.text_unselected);
		mAnimZoomIn = AnimationUtils.loadAnimation(context,
				R.anim.chat_button_zoom_in);
	}
	
	/**
	 * @Description : 设置数据
	 * @auth : sid'pc
	 */
	public void setData(final ArrayList<Integer> filterTimePosition,
	                    final MyCalendarView.CalendarData calendarData, final boolean isOutMeeting)
	{
		new Handler().post(new Runnable()
		{

			@Override
			public void run()
			{
				mFilterTimePosition = filterTimePosition;
				mDurationAdapter.notifyDataSetChanged();
				mMyCalendarView.setData(calendarData);
				if (isOutMeeting)
				{
					setMeetingTypeOut();
				}
			}
		});
	}

	/**
	 * @Description : 设置会议类型
	 * @auth : sid'pc
	 */
	private void setMeetingTypeOut()
	{
		mGvRoom.setVisibility(View.GONE);
		mTvRoom.setVisibility(View.VISIBLE);
		mTvSelelctRoom.setText(getString(R.string.meeting_add_room));
	}

	public class DurationAdapter extends BaseAdapter
	{

		@Override
		public int getCount()
		{
			return 18;
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			Holder holder;
			if (convertView == null)
			{
				convertView = LayoutInflater.from(context).inflate(
						R.layout.grid_item_select_duration, null);
				holder = new Holder();
				holder.tvStartTime = (TextView) convertView
						.findViewById(R.id.tv_hour_start);
				holder.tvEndTime = (TextView) convertView
						.findViewById(R.id.tv_hour_end);
				convertView.setTag(holder);
			}
			else
			{
				holder = (Holder) convertView.getTag();
			}
			String start;
			String end;
			int textPositon = position;
			if (position > 8)
			{
				textPositon = position + 2;
			}
			if (position % 2 == 0)
			{
				start = textPositon / 2 + 8 + ":00—";
				end = textPositon / 2 + 8 + ":30";
			}
			else
			{
				start = textPositon / 2 + 8 + ":30—";
				end = textPositon / 2 + 9 + ":00";
			}

			holder.tvStartTime.setTextColor(Color.BLACK);
			holder.tvEndTime.setTextColor(Color.BLACK);
			if (mFilterTimePosition != null
					&& !mFilterTimePosition.contains(position))
			{
				holder.tvStartTime.setTextColor(mUnselectColor);
				holder.tvEndTime.setTextColor(mUnselectColor);
			}
			holder.tvStartTime.setText(start);
			holder.tvEndTime.setText(end);
			return convertView;
		}

		class Holder
		{
			TextView tvStartTime;

			TextView tvEndTime;
		}

	}

	public class RoomAdapter extends BaseAdapter
	{

		public RoomAdapter()
		{
			// TODO
			mRoomList = new ArrayList<String>();
			mRoomList.add("frostmoune");
			mRoomList.add("starsfury");
			mRoomList.add("sargras");
		}

		@Override
		public int getCount()
		{
			return mRoomList.size();
		}

		@Override
		public Object getItem(int position)
		{
			return null;
		}

		@Override
		public long getItemId(int position)
		{
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			RoomTextView roomTextView;
			if (convertView == null)
			{
				roomTextView = new RoomTextView(context, null);
				convertView = roomTextView;
				convertView.setTag(roomTextView);
				final int selectPosition = position;
				convertView.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View v)
					{
						mSelectRoomPosition = selectPosition;
						notifyDataSetChanged();
					}
				});
			}
			else
			{
				roomTextView = (RoomTextView) convertView.getTag();
			}
			String room = mRoomList.get(position);
			roomTextView.setText(room);
			if (mSelectRoomPosition == position)
			{
				roomTextView.checked(true);
			}
			else
			{
				roomTextView.checked(false);
			}

			return convertView;
		}

		class RoomTextView extends TextView
		{

			public RoomTextView(Context context, AttributeSet attrs)
			{
				super(context, attrs);
				int padding = dp2px(10);

				setTextSize(16);
				setSingleLine();
				setTextColor(Color.BLACK);
				setGravity(Gravity.CENTER);
				setBackgroundColor(Color.WHITE);
				setPadding(0, padding, 0, padding);
			}

			public void checked(boolean isChecked)
			{
				if (isChecked)
				{
					setTextColor(Color.WHITE);
					setBackgroundResource(R.drawable.bg_date_selected);
				}
				else
				{
					setTextColor(Color.BLACK);
					setBackgroundColor(Color.WHITE);
				}
			}

		}

	}
	
	public int dp2px(int dp)
	{
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id)
	{
		MeetingNoFliterSelectFragment.Holder holder = (MeetingNoFliterSelectFragment.Holder) view.getTag();
		if (mSelectTimePosition.contains(position))
		{
			mSelectTimePosition.remove((Integer) position);
			view.setBackgroundColor(Color.WHITE);
			holder.tvStartTime.setTextColor(Color.BLACK);
			holder.tvEndTime.setTextColor(Color.BLACK);
		}
		else
		{
			if (mFilterTimePosition == null
					|| (!mFilterTimePosition.isEmpty() && mFilterTimePosition
					.contains(position)))
			{
				mSelectTimePosition.add(position);
				view.setBackgroundResource(R.drawable.bg_date_selected);
				view.startAnimation(mAnimZoomIn);
				holder.tvStartTime.setTextColor(Color.WHITE);
				holder.tvEndTime.setTextColor(Color.WHITE);
			}
		}

	}
}
