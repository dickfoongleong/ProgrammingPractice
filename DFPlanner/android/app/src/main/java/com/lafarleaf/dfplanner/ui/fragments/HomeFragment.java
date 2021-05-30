package com.lafarleaf.dfplanner.ui.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.lafarleaf.dfplanner.R;
import com.lafarleaf.dfplanner.backend.ServerConnection;
import com.lafarleaf.dfplanner.ui.ContentContainer;
import com.lafarleaf.dfplanner.utils.Subtask;
import com.lafarleaf.dfplanner.utils.Task;
import com.lafarleaf.dfplanner.utils.exceptions.TaskFailedToLoadException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class HomeFragment extends Fragment implements View.OnClickListener {
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
	private static final SimpleDateFormat DB_RETURNED_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US);
	private static final Map<Integer, List<Task>> taskDueDateMap = new HashMap<>();
	private static final Map<Integer, TextView> dateTextViewMap = new HashMap<>();
	
	
	private static Calendar currentDisplayedCalendar;
	private static String username;
	
	private ContentContainer parent;
	private View homeView;
	private View loadingView;
	private boolean isEnabled;
	
	private TextView monthTV;
	private LinearLayout homeTaskLayout;
	private LinearLayout weekTemplateLayout;
	
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		homeView = inflater.inflate(R.layout.fragment_home, container, false);
		
		Bundle args = getArguments();
		if (args != null) {
			username = args.getString("username", "").toLowerCase();
		}
		
		parent = (ContentContainer) getActivity();
		loadingView = homeView.findViewById(R.id.home_loading_scr);

		//TODO: Show views...
		new Thread(this::loadTasks).start();
		setLoadingScreen(true);
		setupTaskListView();
		setupCalendarView();
		
		currentDisplayedCalendar = Calendar.getInstance();
		TextView currentDateTV = dateTextViewMap.get(currentDisplayedCalendar.get(Calendar.DATE));
		if (currentDateTV != null) {
			currentDateTV.performClick();
		}
		
		return homeView;
	}
	
	@Override
	public void onClick(View view) {
		if (isEnabled) {
			int option = view.getId();
			if (option == R.id.add_task_btn) {
				System.out.println("ADD TASK...");
			} else if ( option == R.id.calendar_prev_btn || option == R.id.calendar_next_btn) {
				int valToAdd = option == R.id.calendar_prev_btn ? -1 : 1;
				currentDisplayedCalendar.add(Calendar.MONTH, valToAdd);
				
				new Thread(() -> loadTask(currentDisplayedCalendar.get(Calendar.MONTH), currentDisplayedCalendar.get(Calendar.YEAR))).start();
				
				String month = new SimpleDateFormat("MMM", Locale.US).format(currentDisplayedCalendar.getTime());
				int year = currentDisplayedCalendar.get(Calendar.YEAR);
				monthTV.setText(getString(R.string.month_template, month, year));
				
				setupCalendarWeeks();
				
				Calendar today = Calendar.getInstance();
				if (currentDisplayedCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH) && currentDisplayedCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
					TextView currentDateTV = dateTextViewMap.get(today.get(Calendar.DATE));
					if (currentDateTV != null) {
						currentDateTV.performClick();
					}
				}
			}
		}
	}
	
	private void setupTaskListView() {
		homeTaskLayout = homeView.findViewById(R.id.home_tasks_layout);
		Button addBtn = homeView.findViewById(R.id.add_task_btn);
		addBtn.setOnClickListener(this);
	}
	
	private void setupCalendarView() {
		Calendar today = Calendar.getInstance();
		currentDisplayedCalendar = today;
		
		View calendarTemplateView = homeView.findViewById(R.id.calendar_template);
		TextView prevTV = calendarTemplateView.findViewById(R.id.calendar_prev_btn);
		prevTV.setOnClickListener(this);
		
		String month = new SimpleDateFormat("MMM", Locale.US).format(today.getTime());
		int year = currentDisplayedCalendar.get(Calendar.YEAR);
		monthTV = calendarTemplateView.findViewById(R.id.calendar_month_display);
		monthTV.setText(getString(R.string.month_template, month, year));
		
		TextView nextTv = calendarTemplateView.findViewById(R.id.calendar_next_btn);
		nextTv.setOnClickListener(this);
		
		weekTemplateLayout = calendarTemplateView.findViewById(R.id.calendar_template_week_layout);
		
		setupCalendarWeeks();
	}
	
	private void setupCalendarWeeks() {
		List<List<Integer>> weeks = populateWeeks();
		weekTemplateLayout.removeAllViews();
		dateTextViewMap.clear();
		for (List<Integer> week : weeks) {
			LinearLayout weekLayout = new LinearLayout(getContext());
			weekLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			weekLayout.setOrientation(LinearLayout.HORIZONTAL);
			weekLayout.setBackgroundColor(getResources().getColor(R.color.space_gray, null));
			
			for (int day : week) {
				TextView dayTV = new TextView(getContext());
				dayTV.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
				dayTV.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
				dayTV.setTextColor(getResources().getColor(R.color.white, null));
				dayTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
				dayTV.setTypeface(dayTV.getTypeface(), Typeface.BOLD);
				dayTV.setOnClickListener((view) -> {
					TextView currentSelectedDayTV = dateTextViewMap.get(currentDisplayedCalendar.get(Calendar.DATE));
					if (currentSelectedDayTV != null) {
						currentSelectedDayTV.setBackground(dayTV.getBackground());
					}
					
					currentDisplayedCalendar.set(Calendar.DATE, day);
					displayTaskSummaries(taskDueDateMap.get(day));
					dayTV.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.border_white, null));
				});
				dayTV.setPadding(3, 3, 3, 3);

				if (day == 0) {
					dayTV.setText("");
				} else {
					dayTV.setText(String.valueOf(day));
				}
				weekLayout.addView(dayTV);
				dateTextViewMap.put(day, dayTV);
			}
			weekTemplateLayout.addView(weekLayout);
		}
	}
	
	private void setLoadingScreen(boolean isLoading) {
		if (isLoading) {
			loadingView.setVisibility(View.VISIBLE);
			isEnabled = false;
		} else {
			loadingView.setVisibility(View.GONE);
			isEnabled = true;
		}
	}
	
	private void updateCalendar() {
		for (int date : taskDueDateMap.keySet()) {
			TextView dateTV = dateTextViewMap.get(date);
			if (dateTV != null) {
				dateTV.setTextColor(getResources().getColor(R.color.teal_200, null));
			}
		}
		setLoadingScreen(false);
	}
	
	private void displayTaskSummaries(@Nullable List<Task> tasksToDisplay) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		homeTaskLayout.removeAllViews();
		if (tasksToDisplay != null) {
			for (Task task : tasksToDisplay) {
				View taskSummaryView = inflater.inflate(R.layout.view_task_summary, homeTaskLayout, false);
				if (task.isDone()) {
					taskSummaryView.setBackgroundColor(getResources().getColor(R.color.space_gray, null));
				} else {
					taskSummaryView.setBackgroundColor(Color.parseColor(task.getColor()));
				}
				
				TextView timeTV = taskSummaryView.findViewById(R.id.task_summary_time);
				timeTV.setText(task.getDueDate().toString());
				
				TextView titleTV = taskSummaryView.findViewById(R.id.task_summary_title);
				titleTV.setText(task.getTitle());
				
				CheckBox statusCB = taskSummaryView.findViewById(R.id.task_summary_status);
				statusCB.setChecked(task.isDone());
				statusCB.setOnCheckedChangeListener((btn, isChecked) -> {
					if (isChecked) {
						taskSummaryView.setBackgroundColor(getResources().getColor(R.color.space_gray, null));
					} else {
						taskSummaryView.setBackgroundColor(Color.parseColor(task.getColor()));
					}
				});
				homeTaskLayout.addView(taskSummaryView);
			}
		}
		setLoadingScreen(false);
	}
	
	private void loadTasks() {
		Calendar today = Calendar.getInstance();
		loadTask(today.get(Calendar.MONTH), today.get(Calendar.YEAR));
	}
	
	private void loadTask(int month, int year) {
		if (username == null || username.isEmpty()) {
			new Handler(Looper.getMainLooper()).post(this::updateCalendar);
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month);
		
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date start = calendar.getTime();
		
		calendar.set(Calendar.DATE, calendar.getActualMaximum(Calendar.DATE));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Date end = calendar.getTime();
		
		loadTask(start, end, new TaskLoadCallback() {
			@Override
			public void onSuccess(List<Task> taskList) {
				taskDueDateMap.clear();
				for (Task task : taskList) {
					calendar.setTime(task.getDueDate());
					int day = calendar.get(Calendar.DATE);
					
					List<Task> tasksOfDay;
					if (taskDueDateMap.containsKey(day)) {
						tasksOfDay = taskDueDateMap.get(day);
						if (tasksOfDay != null) {
							tasksOfDay.add(task);
						}
					} else {
						tasksOfDay = new ArrayList<>();
						tasksOfDay.add(task);
						taskDueDateMap.put(day, tasksOfDay);
					}
				}
				new Handler(Looper.getMainLooper()).post(() -> updateCalendar());
			}
			
			@Override
			public void onError(String msg, Throwable cause) {
				Log.e("view_tasks_conn", msg, cause);
				new Handler(Looper.getMainLooper()).post(() -> updateCalendar());
			}
		});
	}
	
	private void loadTask(Date startDate, Date endDate, final TaskLoadCallback callback) {
		String start = DATE_FORMAT.format(startDate);
		String end = DATE_FORMAT.format(endDate);
		
		final List<Task> taskList = new ArrayList<>();
		String urlTemplate = ServerConnection.API_ROOT + "/tasks/gettask/?username=%s&start=%s&end=%s";
		JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
		                                                String.format(urlTemplate, username, start, end),
		                                                null, response -> {
			for (int i = 0; i < response.length(); i++) {
				JSONObject obj = response.optJSONObject(i);
				Task task = new Task();
				task.setCode(obj.optString("code", ""));
				task.setColor(obj.optString("color", ""));
				task.setDone(obj.optBoolean("done", false));
				try {
					task.setDueDate(DB_RETURNED_FORMAT.parse(obj.optString("dueDate")));
				} catch (ParseException pe) {
					Log.e("view_tasks", "Failed On DueDate", pe);
				}
				task.setTitle(obj.optString("title", ""));
				
				List<Subtask> subtaskList = new ArrayList<>();
				JSONArray subtasks = obj.optJSONArray("subtaskList");
				if (subtasks != null) {
					for (int j = 0; j < subtasks.length(); j++) {
						JSONObject subObj = subtasks.optJSONObject(j);
						Subtask subtask = new Subtask();
						subtask.setTitle(subObj.optString("title", ""));
						subtask.setDone(subObj.optBoolean("done", false));
						subtaskList.add(subtask);
					}
				}
				task.setSubtaskList(subtaskList);
				taskList.add(task);
			}
			callback.onSuccess(taskList);
		}, error -> callback.onError(error.getMessage(), new TaskFailedToLoadException("Failed to load task in between " + start + " and " + end)));
		ServerConnection.getInstance(parent).addRequestToQueue(request);
	}
	
	private List<List<Integer>> populateWeeks() {
		int minDate = currentDisplayedCalendar.getActualMinimum(Calendar.DATE);
		int maxDate = currentDisplayedCalendar.getActualMaximum(Calendar.DATE);
		List<List<Integer>> weeks = new ArrayList<>();
	
		while (currentDisplayedCalendar.get(Calendar.DATE) != minDate) {
			currentDisplayedCalendar.add(Calendar.DATE, -1);
		}
		
		int date = minDate;
		List<Integer> firstWeek = new ArrayList<>();
		int dayOfWeek = currentDisplayedCalendar.get(Calendar.DAY_OF_WEEK);
		for (int day = Calendar.SUNDAY; day < dayOfWeek; day++) {
			firstWeek.add(0);
		}
		for (int day = dayOfWeek; day <= Calendar.SATURDAY; day++) {
			firstWeek.add(date);
			date++;
		}
		
		weeks.add(firstWeek);
		while (date <= maxDate) {
			List<Integer> week = new ArrayList<>();
			for (int day = Calendar.SUNDAY; day <= Calendar.SATURDAY; day++) {
				if (date > maxDate) {
					week.add(0);
				} else {
					week.add(date);
					date++;
				}
			}
			weeks.add(week);
		}
		
		return weeks;
	}
	
	private interface TaskLoadCallback {
		void onSuccess(List<Task> taskList);
		void onError(String msg, Throwable cause);
	}
}
