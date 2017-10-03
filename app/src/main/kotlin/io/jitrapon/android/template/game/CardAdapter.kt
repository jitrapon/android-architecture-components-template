package io.jitrapon.android.template.game

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import io.jitrapon.android.template.R

/**
 * Adapters used to manage cards
 *
 * @author Jitrapon Tiachunpun
 */
class CardAdapter(private val viewModel: GameContract.ViewModel) : RecyclerView.Adapter<CardViewHolder>() {

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        viewModel.getCardState(position)?.let {
            holder.apply {
                textView.text = it.text
                exitButton.visibility = if (it.showExitButton) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(parent, viewModel::onCardExitButtonClicked)
    }

    override fun getItemCount() = viewModel.getCardSize()
}

class CardViewHolder(viewGroup: ViewGroup, onExitButtonClicked: ((Int) -> Unit)?) : RecyclerView.ViewHolder(
        LayoutInflater.from(viewGroup.context).inflate(R.layout.card_item, viewGroup, false)) {

    var textView: TextView = itemView.findViewById(R.id.cardTypeText)
    var exitButton: Button = itemView.findViewById(R.id.exitButton)

    init {
        onExitButtonClicked?.let {
            exitButton.setOnClickListener {
                it(adapterPosition)
            }
        }
    }
}