package ru.timuruktus.oridea.View.Other.Adapters;

import java.util.ArrayList;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.timuruktus.newsletters.Model.JSONFragments.Post;
import ru.timuruktus.newsletters.R;

public class UserPostsAdapter extends BaseAdapter {
    Context ctx;
    LayoutInflater lInflater;
    ArrayList<Post> objects;

    UserPostsAdapter(Context context, ArrayList<Post> posts) {
        ctx = context;
        objects = posts;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // кол-во элементов
    @Override
    public int getCount() {
        return objects.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return objects.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }

    // пункт списка
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // используем созданные, но не используемые view
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.post, parent, false);
        }

        Post p = getPost(position);

        // заполняем View в пункте списка данными из поста
        ((TextView) view.findViewById(R.id.title)).setText(p.getTitle());
        ((TextView) view.findViewById(R.id.name)).setText(p.getAuthor());
        ((TextView) view.findViewById(R.id.text)).setText(p.getText());
        Picasso.with(view.getContext())
                .load(p.getAuthorImageUrl())
                .placeholder(R.drawable.user_logo)
                .error(R.drawable.vk_share_button)
                .into((ImageView) view.findViewById(R.id.authorImage));
        Picasso.with(view.getContext())
                .load(p.getPostImageUrl())
                .placeholder(R.drawable.user_logo)
                .error(R.drawable.vk_share_button)
                .into((ImageView) view.findViewById(R.id.postImage));



        return view;
    }

    // товар по позиции
    Post getPost(int position) {
        return ((Post) getItem(position));
    }



}