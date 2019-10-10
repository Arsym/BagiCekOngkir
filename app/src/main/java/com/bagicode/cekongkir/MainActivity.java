package com.bagicode.cekongkir;


import android.app.ProgressDialog;
import android.content.Context;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bagicode.cekongkir.adapter.CityAdapter;
import com.bagicode.cekongkir.adapter.ExpedisiAdapter;
import com.bagicode.cekongkir.adapter.ProvinceAdapter;
import com.bagicode.cekongkir.adapter.SubdistrictAdapter;
import com.bagicode.cekongkir.api.ApiService;
import com.bagicode.cekongkir.api.ApiUrl;
import com.bagicode.cekongkir.model.city.ItemCity;
import com.bagicode.cekongkir.model.cost.ItemCost;
import com.bagicode.cekongkir.model.expedisi.ItemExpedisi;
import com.bagicode.cekongkir.model.province.ItemProvince;
import com.bagicode.cekongkir.model.province.Result;
import com.bagicode.cekongkir.model.subdistrict.ItemSubdistrict;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private EditText etFromProvince, etToProvince;
    private EditText etFromCity, etToCity;
    private EditText etFromSubdistrict, etToSubdistrict;
    private EditText etWeight, etCourier;

    private AlertDialog.Builder alert;
    private AlertDialog ad;
    private EditText searchList;
    private ListView mListView;

    private ProvinceAdapter adapter_province;
    private List<Result> ListProvince = new ArrayList<Result>();

    private CityAdapter adapter_city;
    private List<com.bagicode.cekongkir.model.city.Result> ListCity = new ArrayList<com.bagicode.cekongkir.model.city.Result>();

    private SubdistrictAdapter adapter_subdistrict;
    private List<com.bagicode.cekongkir.model.subdistrict.Result> ListSubdistrict = new ArrayList<com.bagicode.cekongkir.model.subdistrict.Result>();

    private ExpedisiAdapter adapter_expedisi;
    private List<ItemExpedisi> listItemExpedisi = new ArrayList<ItemExpedisi>();

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFromProvince = (EditText) findViewById(R.id.etFromProvince);
        etFromCity = (EditText) findViewById(R.id.etFromCity);
        etFromSubdistrict = (EditText) findViewById(R.id.etFromSubdistrict);
        etToProvince = (EditText) findViewById(R.id.etToProvince);
        etToCity = (EditText) findViewById(R.id.etToCity);
        etToSubdistrict = (EditText) findViewById(R.id.etToSubdistrict);
        etWeight = (EditText) findViewById(R.id.etWeight);
        etCourier = (EditText) findViewById(R.id.etCourier);

        etFromProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpProvince(etFromProvince, etFromCity, etFromSubdistrict);
            }
        });

        etFromSubdistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (etFromCity.getTag().equals("")) {
                        etFromCity.setError("Please choose your form city");
                    } else {
                        popUpSubdistrict(etFromSubdistrict, etFromCity, etFromProvince);
                    }

                } catch (NullPointerException e) {
                    etFromCity.setError("Please choose your form city");
                }

            }
        });

        etFromCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (etFromProvince.getTag().equals("")) {
                        etFromProvince.setError("Please choose your form province");
                    } else {
                        popUpCity(etFromCity, etFromProvince, etFromSubdistrict);
                    }

                } catch (NullPointerException e) {
                    etFromProvince.setError("Please choose your form province");
                }

            }
        });

        etToProvince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpProvince(etToProvince, etToCity, etToSubdistrict);
            }
        });

        etToSubdistrict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (etFromCity.getTag().equals("")) {
                        etFromCity.setError("Please choose your form city");
                    } else {
                        popUpSubdistrict(etToSubdistrict, etToCity, etToProvince);
                    }

                } catch (NullPointerException e) {
                    etToCity.setError("Please choose your form city");
                }

            }
        });

        etToCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (etToProvince.getTag().equals("")) {
                        etToProvince.setError("Please choose your to province");
                    } else {
                        popUpCity(etToCity, etToProvince, etFromSubdistrict);
                    }

                } catch (NullPointerException e) {
                    etToProvince.setError("Please choose your to province");
                }

            }
        });

        etCourier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUpExpedisi(etCourier);
            }
        });

        Button btnProcess = (Button) findViewById(R.id.btnProcess);
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String Origin = etFromSubdistrict.getText().toString();
                String originType = "subdistrict";
                String destination = etToSubdistrict.getText().toString();
                String destinationType = "subdistrict";
                String Weight = etWeight.getText().toString();
                String expedisi = etCourier.getText().toString();

                if (Origin.equals("")){
                    etFromSubdistrict.setError("Please input your origin");
                } else if (destination.equals("")){
                    etToSubdistrict.setError("Please input your destination");
                } else if (Weight.equals("")){
                    etWeight.setError("Please input your Weight");
                } else if (expedisi.equals("")){
                    etCourier.setError("Please input your ItemExpedisi");
                } else {

                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Please wait..");
                    progressDialog.show();

                    getCoast(
                            etFromSubdistrict.getTag().toString(),
                            originType,
                            etToSubdistrict.getTag().toString(),
                            destinationType,
                            etWeight.getText().toString(),
                            etCourier.getText().toString()
                    );
                }

            }
        });
    }



    public void popUpProvince(final EditText etProvince, final EditText etCity, final EditText etSubdistrict ) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertLayout = inflater.inflate(R.layout.custom_dialog_search, null);

        alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("List ListProvince");
        alert.setMessage("select your province");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        ad = alert.show();

        searchList = (EditText) alertLayout.findViewById(R.id.searchItem);
        searchList.addTextChangedListener(new MyTextWatcherProvince(searchList));
        searchList.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mListView = (ListView) alertLayout.findViewById(R.id.listItem);

        ListProvince.clear();
        adapter_province = new ProvinceAdapter(MainActivity.this, ListProvince);
        mListView.setClickable(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = mListView.getItemAtPosition(i);
                Result cn = (Result) o;

                etProvince.setError(null);
                etProvince.setText(cn.getProvince());
                etProvince.setTag(cn.getProvinceId());

                etCity.setText("");
                etCity.setTag("");

                etSubdistrict.setText("");
                etSubdistrict.setTag("");

                ad.dismiss();
            }
        });

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        getProvince();

    }

    public void popUpCity(final EditText etCity, final EditText etProvince, final EditText etSubdistrict) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertLayout = inflater.inflate(R.layout.custom_dialog_search, null);

        alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("List City");
        alert.setMessage("select your city");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        ad = alert.show();

        searchList = (EditText) alertLayout.findViewById(R.id.searchItem);
        searchList.addTextChangedListener(new MyTextWatcherCity(searchList));
        searchList.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mListView = (ListView) alertLayout.findViewById(R.id.listItem);

        ListCity.clear();
        adapter_city = new CityAdapter(MainActivity.this, ListCity);
        mListView.setClickable(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = mListView.getItemAtPosition(i);
                com.bagicode.cekongkir.model.city.Result cn = (com.bagicode.cekongkir.model.city.Result) o;

                etCity.setError(null);
                etCity.setText(cn.getCityName());
                etCity.setTag(cn.getCityId());

                etSubdistrict.setText("");
                etSubdistrict.setTag("");

                ad.dismiss();
            }
        });

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        getCity(etProvince.getTag().toString());

    }

    private void popUpSubdistrict(final EditText etSubdistrict, final EditText etCity, final EditText etProvince) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertLayout = inflater.inflate(R.layout.custom_dialog_search, null);

        alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("List Subdistrict");
        alert.setMessage("select your Subdistrict");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        ad = alert.show();

        searchList = (EditText) alertLayout.findViewById(R.id.searchItem);
        searchList.addTextChangedListener(new MyTextWatcherSubdistrict(searchList));
        searchList.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mListView = (ListView) alertLayout.findViewById(R.id.listItem);

        ListSubdistrict.clear();
        adapter_subdistrict = new SubdistrictAdapter(MainActivity.this, ListSubdistrict);
        mListView.setClickable(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = mListView.getItemAtPosition(i);
                com.bagicode.cekongkir.model.subdistrict.Result cn = (com.bagicode.cekongkir.model.subdistrict.Result) o;

                etSubdistrict.setError(null);
                etSubdistrict.setText(cn.getSubdistrictName());
                etSubdistrict.setTag(cn.getSubdistrictId());

                ad.dismiss();
            }
        });

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        getSubdistrict(etCity.getTag().toString());
    }

    public void popUpExpedisi(final EditText etExpedisi) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View alertLayout = inflater.inflate(R.layout.custom_dialog_search, null);

        alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle("List Expedisi");
        alert.setMessage("select your Expedisi");
        alert.setView(alertLayout);
        alert.setCancelable(true);

        ad = alert.show();

        searchList = (EditText) alertLayout.findViewById(R.id.searchItem);
        searchList.addTextChangedListener(new MyTextWatcherSubdistrict(searchList));
        searchList.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

        mListView = (ListView) alertLayout.findViewById(R.id.listItem);

        listItemExpedisi.clear();
        adapter_expedisi = new ExpedisiAdapter(MainActivity.this, listItemExpedisi);
        mListView.setClickable(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object o = mListView.getItemAtPosition(i);
                ItemExpedisi cn = (ItemExpedisi) o;

                etExpedisi.setError(null);
                etExpedisi.setText(cn.getName());
                etExpedisi.setTag(cn.getId());

                ad.dismiss();
            }
        });

        getExpedisi();

    }

    private class MyTextWatcherProvince implements TextWatcher {

        private View view;

        private MyTextWatcherProvince(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence s, int i, int before, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.searchItem:
                    adapter_province.filter(editable.toString());
                    break;
            }
        }
    }

    private class MyTextWatcherCity implements TextWatcher {

        private View view;

        private MyTextWatcherCity(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence s, int i, int before, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.searchItem:
                    adapter_city.filter(editable.toString());
                    break;
            }
        }
    }

    private class MyTextWatcherSubdistrict implements TextWatcher {

        private View view;


        private MyTextWatcherSubdistrict(View view) { this.view = view; }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence s, int i, int before, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.searchItem:
                    adapter_subdistrict.filter(editable.toString());
                    break;
            }
        }
    }

    public void getProvince() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.URL_ROOT_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<ItemProvince> call = service.getProvince();

        call.enqueue(new Callback<ItemProvince>() {
            @Override
            public void onResponse(Call<ItemProvince> call, Response<ItemProvince> response) {

                progressDialog.dismiss();
                Log.v("wow", "json : " + new Gson().toJson(response));

                if (response.isSuccessful()) {

                    int count_data = response.body().getRajaongkir().getResults().size();
                    for (int a = 0; a <= count_data - 1; a++) {
                        Result itemProvince = new Result(
                                response.body().getRajaongkir().getResults().get(a).getProvinceId(),
                                response.body().getRajaongkir().getResults().get(a).getProvince()
                        );

                        ListProvince.add(itemProvince);
                        mListView.setAdapter(adapter_province);
                    }

                    adapter_province.setList(ListProvince);
                    adapter_province.filter("");

                } else {
                    String error = "Error Retrive Data from Server !!!";
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ItemProvince> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void getCity(String id_province) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.URL_ROOT_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<ItemCity> call = service.getCity(id_province);

        call.enqueue(new Callback<ItemCity>() {
            @Override
            public void onResponse(Call<ItemCity> call, Response<ItemCity> response) {

                progressDialog.dismiss();
                Log.v("wow", "json : " + new Gson().toJson(response));

                if (response.isSuccessful()) {

                    int count_data = response.body().getRajaongkir().getResults().size();
                    for (int a = 0; a <= count_data - 1; a++) {
                        com.bagicode.cekongkir.model.city.Result itemProvince = new com.bagicode.cekongkir.model.city.Result(
                                response.body().getRajaongkir().getResults().get(a).getCityId(),
                                response.body().getRajaongkir().getResults().get(a).getProvinceId(),
                                response.body().getRajaongkir().getResults().get(a).getProvince(),
                                response.body().getRajaongkir().getResults().get(a).getType(),
                                response.body().getRajaongkir().getResults().get(a).getCityName(),
                                response.body().getRajaongkir().getResults().get(a).getPostalCode()
                        );

                        ListCity.add(itemProvince);
                        mListView.setAdapter(adapter_city);
                    }

                    adapter_city.setList(ListCity);
                    adapter_city.filter("");

                } else {
                    String error = "Error Retrive Data from Server !!!";
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ItemCity> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getSubdistrict(String id_city) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.URL_ROOT_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<ItemSubdistrict> call = service.getSubdistrict(id_city);

        call.enqueue(new Callback<ItemSubdistrict>() {
            @Override
            public void onResponse(Call<ItemSubdistrict> call, Response<ItemSubdistrict> response) {

                progressDialog.dismiss();
                Log.v("wow", "json : " + new Gson().toJson(response));

                if (response.isSuccessful()) {

                    int count_data = response.body().getRajaongkir().getResults().size();
                    for (int a = 0; a <= count_data - 1; a++) {
                        com.bagicode.cekongkir.model.subdistrict.Result itemCity = new com.bagicode.cekongkir.model.subdistrict.Result(
                                response.body().getRajaongkir().getResults().get(a).getCityId(),
                                response.body().getRajaongkir().getResults().get(a).getProvinceId(),
                                response.body().getRajaongkir().getResults().get(a).getProvince(),
                                response.body().getRajaongkir().getResults().get(a).getType(),
                                response.body().getRajaongkir().getResults().get(a).getCity(),
                                response.body().getRajaongkir().getResults().get(a).getSubdistrictId(),
                                response.body().getRajaongkir().getResults().get(a).getSubdistrictName()
                        );

                        ListSubdistrict.add(itemCity);
                        mListView.setAdapter(adapter_subdistrict);
                    }

                    adapter_subdistrict.setList(ListSubdistrict);
                    adapter_subdistrict.filter("");

                } else {
                    String error = "Error Retrive Data from Server !!!";
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ItemSubdistrict> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getExpedisi() {

        ItemExpedisi itemItemExpedisi = new ItemExpedisi();

        itemItemExpedisi = new ItemExpedisi("1", "pos");
        listItemExpedisi.add(itemItemExpedisi);
        itemItemExpedisi = new ItemExpedisi("1", "tiki");
        listItemExpedisi.add(itemItemExpedisi);
        itemItemExpedisi = new ItemExpedisi("1", "jne");
        listItemExpedisi.add(itemItemExpedisi);
        itemItemExpedisi = new ItemExpedisi("1", "jnt");
        listItemExpedisi.add(itemItemExpedisi);

        mListView.setAdapter(adapter_expedisi);

        adapter_expedisi.setList(listItemExpedisi);
        adapter_expedisi.filter("");

    }

    public void getCoast(String origin,
                         String originType,
                         String destination,
                         String destinationType,
                         String weight,
                         String courier) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiUrl.URL_ROOT_HTTPS)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService service = retrofit.create(ApiService.class);
        Call<ItemCost> call = service.getCost(
                "3f3bce6f9e0d62d356f48cb8040b5653",
                origin,
                originType,
                destination,
                destinationType,
                weight,
                courier
        );

        call.enqueue(new Callback<ItemCost>() {
            @Override
            public void onResponse(Call<ItemCost> call, Response<ItemCost> response) {

                Log.v("wow", "json : " + new Gson().toJson(response));
                progressDialog.dismiss();

                if (response.isSuccessful()) {

                    int statusCode = response.body().getRajaongkir().getStatus().getCode();

                    if (statusCode == 200){
                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View alertLayout = inflater.inflate(R.layout.custom_dialog_result, null);
                        alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle(response.body().getRajaongkir().getResults().get(0).getName());
                        alert.setMessage(response.body().getRajaongkir().getOriginDetails().getSubdistrictName()+", "+
                                response.body().getRajaongkir().getOriginDetails().getCity()+" Ke "+response.body().getRajaongkir().getDestinationDetails().getSubdistrictName()+", "+
                                response.body().getRajaongkir().getDestinationDetails().getCity());
                        alert.setView(alertLayout);
                        alert.setCancelable(true);

                        ad = alert.show();

                        TextView tv_origin = (TextView) alertLayout.findViewById(R.id.tv_origin);
                        TextView tv_destination = (TextView) alertLayout.findViewById(R.id.tv_destination);
                        TextView tv_expedisi = (TextView) alertLayout.findViewById(R.id.tv_expedisi);
                        TextView tv_coast = (TextView) alertLayout.findViewById(R.id.tv_coast);
                        TextView tv_time = (TextView) alertLayout.findViewById(R.id.tv_time);

                        int count_data = response.body().getRajaongkir().getResults().size();
                        for (int a = 0; a <= count_data - 1; a++) {
                            int count_data2 = response.body().getRajaongkir().getResults().get(a).getCosts().size();
                            for (int b = 0; b <= count_data2 - 1; b++) {

                                tv_expedisi.setText(response.body().getRajaongkir().getResults().get(a).getCosts().get(b).getDescription() + " (" +
                                        response.body().getRajaongkir().getResults().get(a).getCosts().get(b).getService() + ") ");

                                tv_coast.setText("Rp. " + response.body().getRajaongkir().getResults().get(a).getCosts().get(b).getCost().get(0).getValue().toString());

                                tv_time.setText(response.body().getRajaongkir().getResults().get(a).getCosts().get(b).getCost().get(0).getEtd() + " (Days)");
                            }
                        }

                        etCourier.setText("");
                    } else {

                        String message = response.body().getRajaongkir().getStatus().getDescription();
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    String error = "Error Retrive Data from Server !!!";
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ItemCost> call, Throwable t) {

                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Message : Error " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}
