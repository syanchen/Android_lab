package com.example.administrator.lab9.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.lab9.R;
import com.example.administrator.lab9.model.Github;
import com.example.administrator.lab9.model.Repos;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/12/20.
 */

public class CommonAdapter{

    public interface OnItemClickListener {
        void onClick(int position);
        void onLongClick(int position);
    }

    public class FirstAdapter extends RecyclerView.Adapter<FirstAdapter.ViewHolder>{
        private ArrayList<Github> user;
        private OnItemClickListener mOnItemClickListener=null;
        private Context context;

        public FirstAdapter(ArrayList<Github> user, Context context){
            this.user=user;
            this.context=context;
        }

        @Override
        public FirstAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(FirstAdapter.ViewHolder holder, final int position) {
            holder.login.setText(user.get(position).getLogin());
            holder.id.setText("id: "+String.valueOf(user.get(position).getId()));
            holder.blog.setText("blog: "+user.get(position).getBlog());
            Glide.with(context).load(user.get(position).getAvatar_url()).override(90, 90).into(holder.head_pic);
            if(mOnItemClickListener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        mOnItemClickListener.onClick(position);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View v){
                        mOnItemClickListener.onLongClick(position);
                        return false;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return user.size();
        }

        public class ViewHolder  extends RecyclerView.ViewHolder{
            TextView login;
            TextView  id;
            TextView  blog;
            ImageView head_pic;
            public ViewHolder(View itemView) {
                super(itemView);
                login=(TextView)itemView.findViewById(R.id.login);
                id=(TextView)itemView.findViewById(R.id.id);
                blog=(TextView)itemView.findViewById(R.id.blog);
                head_pic=(ImageView)itemView.findViewById(R.id.head_pic);
            }
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener){
            this.mOnItemClickListener=onItemClickListener;
        }
    }

    public class SecondAdapter extends RecyclerView.Adapter<SecondAdapter.ViewHolder>{
        private ArrayList<Repos> blog_list;
        private OnItemClickListener mOnItemClickListener=null;
        private Context context;

        public SecondAdapter(ArrayList<Repos> blog_list, Context context){
            this.blog_list=blog_list;
            this.context=context;
        }

        @Override
        public SecondAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(context).inflate(R.layout.detail_item_layout,parent,false);
            ViewHolder holder=new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(SecondAdapter.ViewHolder holder, final int position) {
            holder.name.setText(blog_list.get(position).getName());
            holder.language.setText(blog_list.get(position).getLanguage());
            holder.des.setText(blog_list.get(position).getDescription());
            if(mOnItemClickListener!=null){
                holder.itemView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        mOnItemClickListener.onClick(position);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                    @Override
                    public boolean onLongClick(View v){
                        mOnItemClickListener.onLongClick(position);
                        return true;
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return blog_list.size();
        }

        public class ViewHolder  extends RecyclerView.ViewHolder{
            TextView name;
            TextView  language;
            TextView  des;
            public ViewHolder(View itemView) {
                super(itemView);
                name=(TextView)itemView.findViewById(R.id.detail_name);
                language=(TextView)itemView.findViewById(R.id.detail_type);
                des=(TextView)itemView.findViewById(R.id.detail_description);
            }
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener){
            this.mOnItemClickListener=onItemClickListener;
        }

    }

}
