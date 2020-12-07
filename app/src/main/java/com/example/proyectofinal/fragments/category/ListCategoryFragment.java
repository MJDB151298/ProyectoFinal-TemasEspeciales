package com.example.proyectofinal.fragments.category;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.proyectofinal.MainMenu;
import com.example.proyectofinal.R;
import com.example.proyectofinal.adapters.ListCategoryAdapter;
import com.example.proyectofinal.connection.dataBaseHelper;
import com.example.proyectofinal.models.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListCategoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListCategoryFragment extends Fragment implements ListCategoryAdapter.ClickOnRowListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private dataBaseHelper dataBaseHelper;
    private ArrayList<Category> elements = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ListCategoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListCategoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListCategoryFragment newInstance(String param1, String param2) {
        ListCategoryFragment fragment = new ListCategoryFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_category, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.category_list);
        FloatingActionButton floatingActionButton = view.findViewById(R.id.flt_addCategory);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainMenu) getActivity()).ChangeToAddCategoryFragment();
            }
        });

        rellenarLista();

        ListCategoryAdapter adapter = new ListCategoryAdapter(getContext(), elements, ListCategoryFragment.this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);
        return view;
    }

    private void rellenarLista() {
        dataBaseHelper = new dataBaseHelper(getContext());
        SQLiteDatabase db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + dataBaseHelper.TABLE_NAME_CATEGOIES, null);

        while (cursor.moveToNext())
        {
            Category category = new Category();
            category.setName(cursor.getString(1));
            category.setImage(cursor.getString(2));

            elements.add(category);
        }

        cursor.close();
    }

    @Override
    public void ClickOnRow(int position) {
//        Intent intent = new Intent(this, CommentsActivity.class);
//        intent.putExtra("Position", position);
//        startActivity(intent);
    }
}