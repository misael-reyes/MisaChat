package com.example.misachat.iu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.misachat.R
import com.example.misachat.databinding.ItemChatBinding
import com.example.misachat.domain.model.Chat

class ChatRecyclerViewAdapter(
    private val chatsList: List<Chat>,
    private val onClickListener: (Chat) -> Unit
) : RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_chat,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val item = chatsList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int = chatsList.size

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val binding = ItemChatBinding.bind(view)

        fun render(item: Chat, onClickListener: (Chat) -> Unit) {
            binding.chatNameText.text = item.name
            binding.usersTextView.text = item.name
            itemView.setOnClickListener { onClickListener(item) }
        }
    }
}