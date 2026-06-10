package com.radmila.businessdirectory.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.radmila.businessdirectory.R;
import com.radmila.businessdirectory.adapter.CompanyAdapter;
import com.radmila.businessdirectory.database.AppDatabase;
import com.radmila.businessdirectory.database.CompanyEntity;
import com.radmila.businessdirectory.model.Company;
import com.radmila.businessdirectory.network.ApiClient;
import com.radmila.businessdirectory.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryFragment extends Fragment {

    private static final String ARG_CATEGORY = "category";
    private String categoryName;

    private ListView listView;
    private EditText etSearch;
    private CompanyAdapter adapter;
    private List<Company> companyList = new ArrayList<>();


    public static CategoryFragment newInstance(String category) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryName = getArguments().getString(ARG_CATEGORY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_category, container, false);

        initViews(view);
        setupAdapter();
        setupSearch();
        fetchCompanies("");

        return view;
    }

    private void initViews(View view) {
        listView = view.findViewById(R.id.listView);
        etSearch = view.findViewById(R.id.etSearch);
    }

    private void setupAdapter() {
        adapter = new CompanyAdapter(requireContext(), companyList);
        listView.setAdapter(adapter);
    }


    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s,
                                          int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s,
                                      int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {

                fetchCompanies(s.toString().trim());
            }
        });
    }



    private void fetchCompanies(String searchTerm) {
        ApiService api = ApiClient.getApiService();
        Call<List<Company>> call;

        if (searchTerm.isEmpty()) {
            call = api.getCompaniesByCategory(categoryName);
        } else {
            call = api.searchCompanies(categoryName, searchTerm);
        }


        call.enqueue(new Callback<List<Company>>() {

            @Override
            public void onResponse(@NonNull Call<List<Company>> call,
                                   @NonNull Response<List<Company>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    updateUI(response.body());

                    cacheToRoom(response.body());
                } else {

                    loadFromRoom(searchTerm);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Company>> call,
                                  @NonNull Throwable t) {

                loadFromRoom(searchTerm);
                if (getContext() != null) {
                    Toast.makeText(getContext(),
                            R.string.no_internet,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void updateUI(List<Company> companies) {
        companyList.clear();
        companyList.addAll(companies);
        adapter.notifyDataSetChanged();
    }



    private void cacheToRoom(List<Company> companies) {

        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());
            List<CompanyEntity> entities = new ArrayList<>();
            for (Company c : companies) {
                entities.add(toEntity(c));
            }
            db.companyDao().insertAll(entities);
        }).start();
    }

    private void loadFromRoom(String searchTerm) {
        new Thread(() -> {
            AppDatabase db = AppDatabase.getInstance(requireContext());

            List<CompanyEntity> cached = searchTerm.isEmpty()
                    ? db.companyDao().getByCategory(categoryName)
                    : db.companyDao().searchInCategory(categoryName, searchTerm);

            List<Company> companies = new ArrayList<>();
            for (CompanyEntity e : cached) {
                companies.add(fromEntity(e));
            }


            if (getActivity() != null) {
                getActivity().runOnUiThread(() -> updateUI(companies));
            }

        }).start();
    }



    private CompanyEntity toEntity(Company c) {
        CompanyEntity e = new CompanyEntity();
        e.setId(c.getId());
        e.setName(c.getName());
        e.setAddress(c.getAddress());
        e.setLatitude(c.getLatitude());
        e.setLongitude(c.getLongitude());
        e.setEmail(c.getEmail());
        e.setPhone(c.getPhone());
        e.setWebsite(c.getWebsite());
        e.setLogoUrl(c.getLogoUrl());
        e.setCategories(c.getCategories());
        return e;
    }

    private Company fromEntity(CompanyEntity e) {
        Company c = new Company();
        c.setId(e.getId());
        c.setName(e.getName());
        c.setAddress(e.getAddress());
        c.setLatitude(e.getLatitude());
        c.setLongitude(e.getLongitude());
        c.setEmail(e.getEmail());
        c.setPhone(e.getPhone());
        c.setWebsite(e.getWebsite());
        c.setLogoUrl(e.getLogoUrl());
        c.setCategories(e.getCategories());
        return c;
    }
}