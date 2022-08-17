package com.example.misachat.iu.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.misachat.R
import com.example.misachat.databinding.ItemMessageBinding
import com.example.misachat.domain.model.Message

class MessageRecyclerViewAdapter(
    private val messagesList: List<Message>,
    private val user: String
): RecyclerView.Adapter<MessageRecyclerViewAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_message,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val item = messagesList[position]
        holder.render(item, user == item.from)
    }

    override fun getItemCount(): Int = messagesList.size

    class MessageViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val binding = ItemMessageBinding.bind(view)

        fun render(item: Message, flag: Boolean) {
            if (flag) {
                // es mi mensaje
                binding.myMessageLayout.visibility = View.VISIBLE
                binding.otherMessageLayout.visibility = View.GONE
                binding.myMessageTextView.text = item.message
            } else {
                // no es mi  mensaje
                binding.myMessageLayout.visibility = View.GONE
                binding.otherMessageLayout.visibility = View.VISIBLE
                binding.othersMessageTextView.text = item.message
            }
        }
    }
}