package com.radmila.businessdirectory.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.radmila.businessdirectory.R;
import com.radmila.businessdirectory.model.Company;
import com.radmila.businessdirectory.network.ApiClient;
import com.radmila.businessdirectory.network.ApiResponse;
import com.radmila.businessdirectory.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCompanyActivity extends AppCompatActivity {


    private TextInputEditText etName, etAddress;
    private TextInputEditText etLatitude, etLongitude;
    private TextInputEditText etEmail, etPhone, etWebsite;


    private CheckBox cbIndustry, cbFun, cbEducation, cbServices;


    private MaterialButton btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);

        initViews();
        setupToolbar();
        setupSaveButton();
    }



    private void initViews() {

        Toolbar toolbar = findViewById(R.id.toolbarAdd);
        setSupportActionBar(toolbar);


        etName      = findViewById(R.id.etName);
        etAddress   = findViewById(R.id.etAddress);
        etLatitude  = findViewById(R.id.etLatitude);
        etLongitude = findViewById(R.id.etLongitude);
        etEmail     = findViewById(R.id.etEmail);
        etPhone     = findViewById(R.id.etPhone);
        etWebsite   = findViewById(R.id.etWebsite);


        cbIndustry  = findViewById(R.id.cbIndustry);
        cbFun       = findViewById(R.id.cbFun);
        cbEducation = findViewById(R.id.cbEducation);
        cbServices  = findViewById(R.id.cbServices);


        btnSave = findViewById(R.id.btnSave);
    }

    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            // Прикажи ја назад стрелката
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Додај компанија");
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Затвори го овој екран
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private void setupSaveButton() {
        btnSave.setOnClickListener(v -> {


            String name      = getText(etName);
            String address   = getText(etAddress);
            String latStr    = getText(etLatitude);
            String lonStr    = getText(etLongitude);
            String email     = getText(etEmail);
            String phone     = getText(etPhone);
            String website   = getText(etWebsite);


            if (!isFormValid(name, address, phone)) {
                return;
            }


            String categories = buildCategoriesString();


            if (categories.isEmpty()) {
                Toast.makeText(this,
                        "Изберете барем една категорија.",
                        Toast.LENGTH_SHORT).show();
                return;
            }


            double latitude  = 0.0;
            double longitude = 0.0;
            try {
                if (!latStr.isEmpty()) latitude  = Double.parseDouble(latStr);
                if (!lonStr.isEmpty()) longitude = Double.parseDouble(lonStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this,
                        "Невалидни координати. Користете децимален број (пр. 41.9981).",
                        Toast.LENGTH_SHORT).show();
                return;
            }


            Company company = new Company();
            company.setName(name);
            company.setAddress(address);
            company.setLatitude(latitude);
            company.setLongitude(longitude);
            company.setEmail(email);
            company.setPhone(phone);
            company.setWebsite(website);
            company.setCategories(categories);


            saveCompanyToServer(company);
        });
    }


    private String getText(TextInputEditText field) {
        if (field.getText() == null) return "";
        return field.getText().toString().trim();
    }


    private boolean isFormValid(String name,
                                String address,
                                String phone) {
        if (TextUtils.isEmpty(name)) {
            etName.setError("Назив е задолжително поле");
            etName.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(address)) {
            etAddress.setError("Адреса е задолжително поле");
            etAddress.requestFocus();
            return false;
        }
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Телефон е задолжително поле");
            etPhone.requestFocus();
            return false;
        }
        return true;
    }


    private String buildCategoriesString() {
        List<String> selected = new ArrayList<>();

        if (cbIndustry.isChecked())  selected.add("industry");
        if (cbFun.isChecked())       selected.add("fun");
        if (cbEducation.isChecked()) selected.add("education");
        if (cbServices.isChecked())  selected.add("services");


        return TextUtils.join(",", selected);
    }


    private void saveCompanyToServer(Company company) {


        btnSave.setEnabled(false);
        btnSave.setText("Зачувување...");

        ApiService api = ApiClient.getApiService();

        api.addCompany(company).enqueue(new Callback<ApiResponse>() {

            @Override
            public void onResponse(@NonNull Call<ApiResponse> call,
                                   @NonNull Response<ApiResponse> response) {


                btnSave.setEnabled(true);
                btnSave.setText(R.string.btn_save);

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isSuccess()) {


                    Toast.makeText(AddCompanyActivity.this,
                            R.string.saved_success,
                            Toast.LENGTH_SHORT).show();


                    finish();

                } else {

                    String msg = (response.body() != null
                            && response.body().getMessage() != null)
                            ? response.body().getMessage()
                            : "Грешка при зачувување. Обидете се повторно.";

                    Toast.makeText(AddCompanyActivity.this,
                            msg, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call,
                                  @NonNull Throwable t) {

                btnSave.setEnabled(true);
                btnSave.setText(R.string.btn_save);

                Toast.makeText(AddCompanyActivity.this,
                        "Нема врска со серверот. Проверете го интернетот.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}