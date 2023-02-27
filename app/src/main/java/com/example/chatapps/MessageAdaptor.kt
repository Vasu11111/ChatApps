package com.example.chatapps

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class MessageAdaptor (val context:Context,val messgesList:ArrayList<Message>)
    :RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEVIED=1
    val ITEM_SAND=2

    class SandViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val sandMessage = itemView.findViewById<TextView>(R.id.txt_sand_message)

    }

    class ReceiveViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

        val receiveMessage= itemView.findViewById<TextView>(R.id.txt_received_message)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==1){
            //infelet recevied
            val view:View= LayoutInflater.from(context).inflate(R.layout.received,parent,false)
            return ReceiveViewHolder(view)

        }else{
            //infelet sand

            val view:View= LayoutInflater.from(context).inflate(R.layout.sand,parent,false)
            return SandViewHolder(view)
        }


    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentmessge=messgesList[position]


        if (holder.javaClass==SandViewHolder::class.java){


//            sand viewholder
            val viewHolder=holder as SandViewHolder
            holder.sandMessage.text=currentmessge.message

        }else{
//            //recevide viewholder
            val viewHolder=holder as ReceiveViewHolder
            holder.receiveMessage.text=currentmessge.message


        }

    }

    override fun getItemViewType(position: Int): Int {

        val currentMessage=messgesList[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.sendId)){
            return ITEM_SAND
        }else{
            return ITEM_RECEVIED
        }

    }

    override fun getItemCount(): Int {
        return messgesList.size
    }


}