package com.ae.apps.tripmeter.fragments;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ae.apps.common.views.RoundedImageView;
import com.ae.apps.tripmeter.R;
import com.ae.apps.tripmeter.listeners.ExpensesInteractionListener;
import com.ae.apps.tripmeter.managers.ExpenseManager;
import com.ae.apps.tripmeter.models.Trip;
import com.ae.apps.tripmeter.models.TripExpense;
import com.ae.apps.tripmeter.utils.AppConstants;
import com.ae.apps.tripmeter.views.adapters.ExpensesPagerAdapter;

/**
 * Activities that contain this fragment must implement the
 * {@link ExpensesInteractionListener} interface
 * to handle interaction events.
 */
public class TripDetailsFragment extends Fragment
        implements AddExpenseDialogFragment.AddExpenseDialogListener {

    private String mTripId;
    private Trip mTrip;
    private ExpenseManager mExpenseManager;
    private LinearLayout mTripMembersContainer;

    private ViewPager mViewPager;

    public TripDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * @return A new instance of fragment TripDetailsFragment.
     */
    public static TripDetailsFragment newInstance() {
        return new TripDetailsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_trip_details, container, false);

        if (null == getArguments() && null == savedInstanceState) {
            throw new IllegalArgumentException("TripId is required");
        }

        if (null != getArguments()) {
            mTripId = getArguments().getString(AppConstants.KEY_TRIP_ID);
        }

        if (null != savedInstanceState) {
            mTripId = savedInstanceState.getString(AppConstants.KEY_TRIP_ID);
        }

        initViews(inflatedView);

        return inflatedView;
    }

    private void initViews(View inflatedView) {
        mExpenseManager = ExpenseManager.newInstance(getContext());
        mTrip = mExpenseManager.getTripByTripId(String.valueOf(mTripId));

        // Update the trip with the ContactVos from member ids
        mTrip.getMembers().addAll(mExpenseManager.getContactsFromIds(mTrip.getMemberIds()));

        TextView tripName = (TextView) inflatedView.findViewById(R.id.txtTripName);
        TextView tripDate = (TextView) inflatedView.findViewById(R.id.txtTripDate);
        mTripMembersContainer = (LinearLayout) inflatedView.findViewById(R.id.tripMembersContainer);

        addTripMembersToContainer();

        tripName.setText(mTrip.getName());

        // Display total trip expenses below trip name
        tripDate.setText(getString(R.string.str_total_expenses) + " : " + mExpenseManager.getTotalTripexpenses(mTripId));

        FloatingActionButton floatingActionButton = (FloatingActionButton) inflatedView.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddExpenseDialog();
            }
        });

        mViewPager = (ViewPager) inflatedView.findViewById(R.id.viewpager);
        setUpViewPager();

        TabLayout mTabLayout = (TabLayout) inflatedView.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        // Bundle args = new Bundle();
        // args.putString(AppConstants.KEY_TRIP_ID, mTripId);
        // Fragment fragment = TripExpenseFragment.newInstance(args);
        // FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        // transaction.replace(R.id.frag, fragment).commit();
    }

    private void addTripMembersToContainer() {
        String[] memberIds = mTrip.getMemberIds().split(AppConstants.CONTACT_ID_SEPARATOR);
        RoundedImageView roundedImageView;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(5, 5, 5, 5);
        for (String memberId : memberIds) {
            roundedImageView = new RoundedImageView(getContext());
            roundedImageView.setImageDrawable(
                    new BitmapDrawable(getResources(), mExpenseManager.getContactPhoto(memberId))
            );
            mTripMembersContainer.addView(roundedImageView, layoutParams);
        }
    }

    private void setUpViewPager() {
        Bundle args = new Bundle();
        args.putString(AppConstants.KEY_TRIP_ID, mTripId);

        ExpensesPagerAdapter pagerAdapter = new ExpensesPagerAdapter(getChildFragmentManager());
        pagerAdapter.addFragment(TripExpenseFragment.newInstance(args), getResources().getString(R.string.str_expenses));
        pagerAdapter.addFragment(TripMemberShareFragment.newInstance(args), getResources().getString(R.string.str_share));
        // TODO Add fragment showing total Spending per member
        mViewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(AppConstants.KEY_TRIP_ID, mTripId);
    }

    /**
     * See https://guides.codepath.com/android/Using-DialogFragment#passing-data-to-parent-fragment
     */
    private void showAddExpenseDialog() {
        FragmentManager fragmentManager = getFragmentManager();
        AddExpenseDialogFragment dialogFragment = AddExpenseDialogFragment.newInstance(mTrip);
        dialogFragment.setTargetFragment(TripDetailsFragment.this, 300);
        dialogFragment.show(fragmentManager, "fragment_add_expense");
    }

    @Override
    public void onExpenseAdded(TripExpense tripExpense) {
        mExpenseManager.addExpense(tripExpense);
        // TODO Refresh the expense list
    }
}
