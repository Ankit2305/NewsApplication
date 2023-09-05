package com.example.newsapplicationmoengage.moe

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapplicationmoengage.R
import com.example.newsapplicationmoengage.databinding.CustomInboxItemBinding
import com.example.newsapplicationmoengage.helper.DateParser
import com.moengage.inbox.core.model.InboxMessage
import com.moengage.inbox.ui.adapter.InboxAdapter
import com.moengage.inbox.ui.adapter.InboxListAdapter
import com.moengage.inbox.ui.adapter.ViewHolder

class CustomInboxAdapter: InboxAdapter() {
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int, inboxMessage: InboxMessage): Int {
        return 0
    }

    override fun onBindViewHolder(
        viewHolder: ViewHolder,
        position: Int,
        inboxMessage: InboxMessage,
        inboxListAdapter: InboxListAdapter
    ) {
        val bindings = CustomInboxItemBinding.bind(viewHolder.itemView)
        val payload = inboxMessage.payload
        Log.i("CustomIATag", "$payload")
        bindings.apply {
            titleTextView.text = payload.getString("gcm_title")
            messageTextView.text = payload.getString("gcm_alert")
            timeStamp.text = DateParser.parseDate(payload.getLong("MOE_MSG_RECEIVED_TIME"))
            image.clipToOutline = true

            deleteBin.setOnClickListener {
                inboxListAdapter.deleteItem(position, inboxMessage)
            }

            root.setOnClickListener {
                try {
                    //Notify SDK about item click
                    inboxListAdapter.onItemClicked(position, inboxMessage)

                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(payload.getString("gcm_webUrl"))
                    }
                    root.context.startActivity(intent)
                } catch(e: Exception) {
                    e.printStackTrace()
                }
            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.custom_inbox_item, viewGroup, false)
        )
    }

}