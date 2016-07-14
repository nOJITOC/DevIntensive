package com.softdesign.devintensive.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.softdesign.devintensive.R;
import com.softdesign.devintensive.data.storage.models.UserDTO;
import com.softdesign.devintensive.ui.adapters.RepositoriesAdapter;
import com.softdesign.devintensive.utils.ConstantManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileUserActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.bio_et)
    EditText mUserBio;
    @BindView(R.id.user_photo_img)
    ImageView mProfileImage;
    @BindView(R.id.dev_rating_txt)
    TextView mUserRating;
    @BindView(R.id.dev_code_lines_txt)
    TextView mUserCodeLines;
    @BindView(R.id.dev_projects_txt)
    TextView mUserProjects;
    @BindView(R.id.repositories_list)
    ListView mRepoListView;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout mCollapsingToolbar;
    @BindView(R.id.main_coordinator_container)
    CoordinatorLayout mCoordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        ButterKnife.bind(this);
        setupToolbar();
        initProfileData();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initProfileData() {
        UserDTO userDTO = getIntent().getParcelableExtra(ConstantManager.PARCELABLE_KEY);
        final List<String> repositories = userDTO.getRepositories();
        final RepositoriesAdapter repositoriesAdapter = new RepositoriesAdapter(this, repositories);
        mRepoListView.setAdapter(repositoriesAdapter);

        mRepoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar.make(mCollapsingToolbar, "Репозиторий " +repositories.get(i),Snackbar.LENGTH_LONG).show();
                String forUri = repositories.get(i);
                if(forUri.contains("https://"))
                    forUri=forUri.substring(forUri.indexOf("https://")+"https://".length());
                if(forUri.contains("http://"))
                    forUri=forUri.substring(forUri.indexOf("http://")+"http://".length());
                Uri uri=Uri.parse(ConstantManager.INTENT_SCHEME[3]+forUri);
                Intent showRepo = new Intent(Intent.ACTION_DEFAULT,uri);
                startActivity(showRepo);
            }
        });
        mUserBio.setText(userDTO.getBio());
        mUserRating.setText(userDTO.getRating());
        mUserCodeLines.setText(userDTO.getCodeLines());
        mUserProjects.setText(userDTO.getProjects());
        mCollapsingToolbar.setTitle(userDTO.getFullName());
        Glide.with(this)
                .load(userDTO.getPhoto())
                .skipMemoryCache(true)
                .placeholder(R.drawable.user_bg)
                .error(R.drawable.user_bg)

        .into(mProfileImage)
        ;

    }
}
