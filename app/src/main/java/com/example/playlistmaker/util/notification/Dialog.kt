import android.content.Context
import android.content.DialogInterface
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import androidx.core.content.ContextCompat
import com.example.playlistmaker.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class Dialog(context: Context) : MaterialAlertDialogBuilder(context, R.style.customMaterialAlertDialog) {

    private val backgroundColor = ContextCompat.getColor(context, R.color.yp_white)
    private val titleTextColor = ContextCompat.getColor(context, R.color.yp_black)

        init {
            setBackground(ColorDrawable(backgroundColor))
        }

    override fun setTitle(title: CharSequence?): MaterialAlertDialogBuilder {
        return super.setTitle(title?.let { getBoldColoredText(it) })
    }

    override fun setMessage(message: CharSequence?): MaterialAlertDialogBuilder {
        return super.setMessage(message?.let { getColoredText(it) })
    }

    fun setPositiveButton(
        text: CharSequence,
        listener: DialogInterface.OnClickListener?
    ): Dialog {
        super.setPositiveButton(getColoredText(text), listener)
        return this
    }

    fun setNegativeButton(
        text: CharSequence,
        listener: DialogInterface.OnClickListener?
    ): Dialog {
        super.setNegativeButton(getColoredText(text), listener)
        return this
    }

    fun setNeutralButton(
        text: CharSequence,
        listener: DialogInterface.OnClickListener?
    ): Dialog {
        super.setNeutralButton(getColoredText(text), listener)
        return this
    }

    private fun getBoldColoredText(text: CharSequence): SpannableString {
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            ForegroundColorSpan(titleTextColor),
            0,
            text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }

    private fun getColoredText(text: CharSequence): SpannableString {
        val spannableString = SpannableString(text)
        spannableString.setSpan(
            ForegroundColorSpan(titleTextColor),
            0,
            text.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return spannableString
    }
}