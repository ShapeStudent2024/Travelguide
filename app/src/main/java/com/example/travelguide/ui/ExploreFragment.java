package com.example.travelguide.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travelguide.attraction.AttractionCardAdapter;
import com.example.travelguide.attraction.AttractionLocation;
import com.example.travelguide.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExploreFragment extends Fragment implements OnMapReadyCallback {

    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static final int REQUEST_SPEECH_INPUT = 100;
    private static final int REQUEST_LOCATION_PERMISSION = 300;

    private MapView mapView;
    private GoogleMap googleMap;
    private SearchView searchView;
    private ImageButton voiceButton;
    private FloatingActionButton myLocationButton;
    private RecyclerView cardRecyclerView;
    private AttractionCardAdapter cardAdapter;
    private PagerSnapHelper snapHelper;

    private FusedLocationProviderClient fusedLocationClient;
    private List<AttractionLocation> attractions;
    private List<Marker> markers;
    private Marker currentLocationMarker;
    private int currentAttractionIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_explore, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        setupSearchView();
        setupVoiceButton();
        setupMyLocationButton();
        setupCardRecyclerView();
        initAttractionData();
        initLocationClient();

        // 初始化地图
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    private void initViews(View view) {
        mapView = view.findViewById(R.id.map_view);
        searchView = view.findViewById(R.id.search_view);
        voiceButton = view.findViewById(R.id.voice_button);
        myLocationButton = view.findViewById(R.id.my_location_button);
        cardRecyclerView = view.findViewById(R.id.card_recycler_view);
        markers = new ArrayList<>();
    }

    private void setupSearchView() {
        searchView.setQueryHint("Search attractions...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAttractions(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 2) {
                    searchAttractions(newText);
                }
                return true;
            }
        });
    }

    private void setupVoiceButton() {
        voiceButton.setOnClickListener(v -> {
            if (checkAudioPermission()) {
                startVoiceInput();
            } else {
                requestAudioPermission();
            }
        });
    }

    private void setupMyLocationButton() {
        myLocationButton.setOnClickListener(v -> {
            if (checkLocationPermission()) {
                getCurrentLocation();
            } else {
                requestLocationPermission();
            }
        });
    }

    private void initLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
    }

    private boolean checkAudioPermission() {
        return ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestAudioPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.RECORD_AUDIO},
                REQUEST_RECORD_AUDIO_PERMISSION);
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                REQUEST_LOCATION_PERMISSION);
    }

    private void getCurrentLocation() {
        if (!checkLocationPermission()) {
            requestLocationPermission();
            return;
        }

        myLocationButton.setEnabled(false);

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        myLocationButton.setEnabled(true);

                        if (location != null) {
                            LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                            // 移动地图到当前位置
                            if (googleMap != null) {
                                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));

                                // 清除之前的当前位置标记
                                if (currentLocationMarker != null) {
                                    currentLocationMarker.remove();
                                }

                                // 添加当前位置标记
                                currentLocationMarker = googleMap.addMarker(new MarkerOptions()
                                        .position(currentLocation)
                                        .title("Your Location")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                                Toast.makeText(getContext(), "Your current location has been located", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Unable to get current location, please check GPS settings", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    myLocationButton.setEnabled(true);
                    Toast.makeText(getContext(), "Positioning failed：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please tell me what you want to search for...");

        try {
            startActivityForResult(intent, REQUEST_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(getContext(), "Your device does not support voice input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SPEECH_INPUT && resultCode == getActivity().RESULT_OK && data != null) {
            ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (result != null && !result.isEmpty()) {
                String spokenText = result.get(0);
                searchView.setQuery(spokenText, true);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startVoiceInput();
                } else {
                    Toast.makeText(getContext(), "Microphone permission is required to use voice search", Toast.LENGTH_SHORT).show();
                }
                break;

            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    Toast.makeText(getContext(), "Location permission is required to obtain current location", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void setupCardRecyclerView() {
        // 设置水平滑动布局
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        cardRecyclerView.setLayoutManager(layoutManager);

        // 添加分页效果
        snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(cardRecyclerView);

        // 设置滚动监听器
        cardRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 获取当前居中的卡片位置
                    View centerView = snapHelper.findSnapView(layoutManager);
                    if (centerView != null) {
                        int position = layoutManager.getPosition(centerView);
                        if (position != currentAttractionIndex) {
                            currentAttractionIndex = position;
                            updateMapForCurrentAttraction();
                            updateMarkerSizes();
                        }
                    }
                }
            }
        });
    }

    private void initAttractionData() {
        attractions = new ArrayList<>();

        // 添加景点数据
        attractions.add(new AttractionLocation(
                "中環半山扶手電梯", "Central-Mid-Levels Escalators",
                new LatLng(22.2838450, 114.1549680),
                "The world's longest outdoor covered escalator system",
                4.2f, "1.2km", R.drawable.escalator_placeholder,
                AttractionLocation.Type.TRANSPORTATION
        ));

        attractions.add(new AttractionLocation(
                "山頂纜車", "The Peak Tram",
                new LatLng(22.2776830, 114.1591870),
                "Hong Kong's famous cable car system, heading to the top of Victoria Peak",
                4.5f, "2.8km", R.drawable.peak_tram_placeholder,
                AttractionLocation.Type.TRANSPORTATION
        ));

        attractions.add(new AttractionLocation(
                "星光大道", "Avenue of Stars HK",
                new LatLng(22.2930040, 114.1741480),
                "A waterfront promenade commemorating the Hong Kong film industry",
                4.0f, "0.8km", R.drawable.avenue_stars_placeholder,
                AttractionLocation.Type.SIGHTSEEING
        ));

        attractions.add(new AttractionLocation(
                "香港迪士尼樂園", "Hong Kong Disneyland",
                new LatLng(22.3129670, 114.0412820),
                "The second Disney theme park in Asia",
                4.6f, "15.2km", R.drawable.hong_kong_disneyland,
                AttractionLocation.Type.THEME_PARK
        ));

        attractions.add(new AttractionLocation(
                "石板街", "Stone Slab Street",
                new LatLng(22.2823670, 114.1549300),
                "The only remaining cobblestone street in Hong Kong",
                3.8f, "1.0km", R.drawable.stone_street_placeholder,
                AttractionLocation.Type.SIGHTSEEING
        ));

        attractions.add(new AttractionLocation(
                "蘭桂坊", "Lan Kwai Fong",
                new LatLng(22.2810680, 114.1554790),
                "Hong Kong's famous nightlife area",
                4.1f, "0.9km", R.drawable.lan_kwai_fong_placeholder,
                AttractionLocation.Type.ENTERTAINMENT
        ));

        // 设置适配器
        cardAdapter = new AttractionCardAdapter(attractions, this::onAddToPlanClicked);
        cardRecyclerView.setAdapter(cardAdapter);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap map) {
        googleMap = map;

        // 設置地圖初始位置（香港中心）
        LatLng hongKongCenter = new LatLng(22.28, 114.15);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(hongKongCenter, 12f));

        // 启用我的位置图层（如果有权限）
        if (checkLocationPermission()) {
            googleMap.setMyLocationEnabled(true);
        }

        // 添加景點標記
        addAttractionMarkers();

        // 設置地圖點擊監聽
        googleMap.setOnMarkerClickListener(marker -> {
            // 检查是否是当前位置标记
            if (marker.equals(currentLocationMarker)) {
                return true;
            }

            AttractionLocation attraction = (AttractionLocation) marker.getTag();
            if (attraction != null) {
                int index = attractions.indexOf(attraction);
                if (index != -1) {
                    scrollToAttraction(index);
                }
            }
            return true;
        });

        // 移动到第一个景点
        if (!attractions.isEmpty()) {
            updateMapForCurrentAttraction();
            updateMarkerSizes();
        }
    }

    private void addAttractionMarkers() {
        markers.clear();
        for (AttractionLocation attraction : attractions) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(attraction.getLatLng())
                    .title(attraction.getChineseName())
                    .snippet(attraction.getEnglishName())
                    .icon(BitmapDescriptorFactory.defaultMarker(getMarkerColor(attraction.getType())));

            Marker marker = googleMap.addMarker(markerOptions);
            if (marker != null) {
                marker.setTag(attraction);
                markers.add(marker);
            }
        }
    }

    private float getMarkerColor(AttractionLocation.Type type) {
        switch (type) {
            case THEME_PARK:
                return BitmapDescriptorFactory.HUE_RED;      // 紅色 - 遊樂園
            case TRANSPORTATION:
                return BitmapDescriptorFactory.HUE_BLUE;     // 藍色 - 交通景點
            case SIGHTSEEING:
                return BitmapDescriptorFactory.HUE_GREEN;    // 綠色 - 觀光景點
            case ENTERTAINMENT:
                return BitmapDescriptorFactory.HUE_VIOLET;   // 紫色 - 娛樂區域
            default:
                return BitmapDescriptorFactory.HUE_RED;
        }
    }

    private void updateMapForCurrentAttraction() {
        if (currentAttractionIndex >= 0 && currentAttractionIndex < attractions.size()) {
            AttractionLocation attraction = attractions.get(currentAttractionIndex);
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(attraction.getLatLng(), 15f));
        }
    }

    private void updateMarkerSizes() {
        // 这里可以实现标记大小的变化效果
        // Google Maps Android API 默认不支持动态改变标记大小
        // 可以通过重新创建标记或使用自定义标记来实现
    }

    private void scrollToAttraction(int index) {
        if (index >= 0 && index < attractions.size()) {
            currentAttractionIndex = index;

            // 滚动到指定位置
            LinearLayoutManager layoutManager = (LinearLayoutManager) cardRecyclerView.getLayoutManager();
            if (layoutManager != null) {
                layoutManager.scrollToPositionWithOffset(index,
                        (cardRecyclerView.getWidth() - getResources().getDimensionPixelSize(R.dimen.card_width)) / 2);
            }

            updateMapForCurrentAttraction();
        }
    }

    private void searchAttractions(String query) {
        for (int i = 0; i < attractions.size(); i++) {
            AttractionLocation attraction = attractions.get(i);
            if (attraction.getChineseName().contains(query) ||
                    attraction.getEnglishName().toLowerCase().contains(query.toLowerCase())) {
                scrollToAttraction(i);
                break;
            }
        }
    }

    private void onAddToPlanClicked(AttractionLocation attraction) {
        // TODO: 实现添加到行程计划的逻辑
        if (getContext() != null) {
            Toast.makeText(getContext(),
                    "Added " + attraction.getChineseName() + " to trip plan",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // MapView 生命周期管理
    @Override
    public void onResume() {
        super.onResume();
        if (mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }
}