package com.playground.albazip.src.mypage.manager.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.playground.albazip.R
import com.playground.albazip.databinding.ItemRvAddToDoBinding
import com.playground.albazip.src.mypage.manager.workerlist.data.local.TodoData


class ToDoListAdapter(val itemList: ArrayList<TodoData>,val context:Context) :
    RecyclerView.Adapter<ToDoListAdapter.ToDoHolder>() {

    private lateinit var binding: ItemRvAddToDoBinding

    private val myContext = context

    // 아이템 추가
    fun addItem(todoData: TodoData) {
        itemList.add(itemCount, todoData)
        this.notifyItemInserted(itemCount)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoHolder {
        binding = ItemRvAddToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ToDoHolder(binding)
    }

    override fun onBindViewHolder(holder: ToDoHolder, position: Int) {

        holder.setIsRecyclable(false)

        holder.binding.etToDoName.setText(itemList[position].title)
        holder.binding.etToDoName.addTextChangedListener(titleTextWatcher(position))


        holder.binding.etToDoExplain.setText(itemList[position].context)
        holder.binding.etToDoExplain.addTextChangedListener(contentTextWatcher(position))


        holder.binding.ibtnExit.setOnClickListener { // x 버튼 누르면 없애기

            // 텍스트 내용 초기화
            itemList.removeAt(position)
            notifyDataSetChanged()
            // holder.binding.llRoot.removeViewAt(position)
            // notifyItemRemoved(position)
        }

        // 텍스트 입력 여부 감지
        // focus 감지
        holder.binding.etToDoName.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                holder.binding.llRoot.background = ContextCompat.getDrawable(
                    myContext,
                    R.drawable.bg_to_do_rv_active
                ) else {
                holder.binding.llRoot.background = ContextCompat.getDrawable(
                    myContext,
                    R.drawable.bg_to_do_rv
                )
            }
        }

        holder.binding.etToDoExplain.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                holder.binding.llRoot.background = ContextCompat.getDrawable(
                    myContext,
                    R.drawable.bg_to_do_rv_active
                ) else {
                holder.binding.llRoot.background = ContextCompat.getDrawable(
                    myContext,
                    R.drawable.bg_to_do_rv
                )
            }
        }

    }

    override fun getItemCount(): Int = itemList.size

    inner class ToDoHolder(val binding: ItemRvAddToDoBinding) :
        RecyclerView.ViewHolder(binding.root) {

    }

    inner class titleTextWatcher(var position: Int) : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            itemList[position].title = p0.toString()
        }
    }

    inner class contentTextWatcher(var position: Int) : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            itemList[position].context = p0.toString()
        }
    }


}