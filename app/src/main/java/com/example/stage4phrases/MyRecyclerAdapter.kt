package com.example.stage4phrases

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stage4phrases.data.PhraseDao
import com.example.stage4phrases.data.model.Phrase

class MyRecyclerViewAdapter(data: List<Phrase>, val phraseDao: PhraseDao) :
    RecyclerView.Adapter<TransactionViewHolder>() {

    var data = data
    set(value)  {
        field = value
        notifyDataSetChanged()
    }

    private fun deletePhrase(phrase: Phrase) {
        phraseDao.delete(phrase)
        data = phraseDao.getAll()
        /*val index = data.indexOf(phrase)
        data.remove(phrase)
        notifyItemRemoved(index)*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        //infla la vista pasandole el layout que queremos que sea el item del recyclerView
        return TransactionViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_phrase, parent, false)
        )

    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val phrase = data[position]
        holder.tvPhrase.text = phrase.phrase

        holder.tvDelete.setOnClickListener {
            Log.d("click", "textViewDelete")
            deletePhrase(phrase = phrase)
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

}

class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val tvPhrase: TextView = view.findViewById(R.id.phraseTextView)
    val tvDelete: TextView = view.findViewById(R.id.deleteTextView)

}