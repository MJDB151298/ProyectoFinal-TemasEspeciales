package com.example.proyectofinal.fragments.products;

import android.os.Bundle;
import android.view.*;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinal.R;
import com.example.proyectofinal.adapters.ListProductAdapter;
import com.example.proyectofinal.helpers.ButtonHelper;
import com.example.proyectofinal.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListProductFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListProductFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListProductFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListProductFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListProductFragment newInstance(String param1, String param2) {
        ListProductFragment fragment = new ListProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentº
        View view = inflater.inflate(R.layout.fragment_list_product, container, false);

        //Obteniendo la informacion necesaria a traves de la base de datos
        List<Product> productList = new ArrayList<>();//Product.getProducts(this.getContext());
        //TODO: Llenar productos con base de datos - Marcos
        System.out.println("El size de productos es: " + productList.size());

        //Mostrando el float action button
        //ButtonHelper.SwitchCallCreateProductButton((FloatingActionButton) getActivity().findViewById(R.id.callCreateProductButton), false);

        RecyclerView recyclerView = view.findViewById(R.id.productRecyclerView);
        ListProductAdapter productListAdapter = new ListProductAdapter(this.getContext(), productList, this.getActivity());
        recyclerView.setAdapter(productListAdapter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        return view;
    }
}