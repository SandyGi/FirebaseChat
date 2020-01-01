/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

@Suppress("UNCHECKED_CAST")
abstract class BaseAdapter<T, V>(
    private val mContext: Context,
    private var mArrayList: ArrayList<T>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    abstract val layoutResId: Int

    //    public abstract void onBindData(RecyclerView.ViewHolder holder, T model, int position, D dataBinding);
    abstract fun onBindData(model: T, position: Int, dataBinding: V)

    abstract fun onItemClick(model: T, position: Int)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val dataBinding = LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)
        return ItemViewHolder(dataBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        onBindData(
            mArrayList!![position],
            position,
            (holder as BaseAdapter<T, V>.ItemViewHolder).mDataBinding
        )

        (holder.mDataBinding as View).setOnClickListener {
            onItemClick(
                mArrayList!![position],
                position
            )
        }
    }

    override fun getItemCount(): Int {
        return mArrayList!!.size
    }

    fun addItems(arrayList: ArrayList<T>) {
        mArrayList = arrayList
        this.notifyDataSetChanged()
    }

    fun getItem(position: Int): T {
        return mArrayList!![position]
    }

    fun remove(position: Int) {
        mArrayList!!.removeAt(position)
        this.notifyDataSetChanged()
    }

    internal inner class ItemViewHolder(binding: View) : RecyclerView.ViewHolder(binding) {
        var mDataBinding: V

        init {
            mDataBinding = binding as V
        }
    }
}