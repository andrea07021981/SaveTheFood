package com.example.savethefood.util

import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.*
import android.widget.AdapterView.*
import androidx.annotation.RequiresApi
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.savethefood.R
import com.example.savethefood.addfood.FoodTypeAdapter
import com.example.savethefood.data.domain.FoodItem
import com.example.savethefood.data.domain.ProductDomain
import com.example.savethefood.food.FoodSearchAdapter
import com.google.android.material.textfield.TextInputEditText
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


/**
 * Needs to be used with [NumberOfSetsConverters.setArrayToString].
 */
@BindingAdapter("bind:numberOfSets")
fun setNumberOfSets(view: EditText, value: String) {
    view.setText(value)
}

@BindingAdapter("bind:listFoods")
fun bindRecycleView(recyclerView: RecyclerView, data: List<ProductDomain>?) {
    val adapter = recyclerView.adapter as FoodSearchAdapter
    adapter.submitList(data)
}

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("bind:imageFoodUrl")
fun bindFoodImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}

@BindingAdapter("bind:htmlConverter")
fun TextView.bindFoodDescription(html: String?) {//TODO check if we can directly bind in xml with binding exp https://developer.android.com/topic/libraries/data-binding/expressions
    html?.let { text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY); }
}

@BindingAdapter("bind:foodListData")
fun bindFoodListData(recyclerView: RecyclerView, data: Collection<FoodItem>?) {
    data?.let {
        val adapter = recyclerView.adapter as FoodTypeAdapter
        adapter.submitList(data.toList())
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@BindingAdapter("bind:year", "bind:month", "bind:day", "bind:onDateChanged")
fun bindDate(
    view: DatePicker, year: Int, month: Int,
    day: Int, block: (Calendar) -> Unit
) {
    view.init(
        year, month, day
    ) { _, currentYear, monthOfYear, dayOfMonth ->
        val calendar = Calendar.getInstance().apply {
            set(currentYear, monthOfYear, dayOfMonth)
            time
        }
        block(calendar)
    }
}

@BindingAdapter("bind:doubleToString")
fun TextInputEditText.bindTextDouble(value: Double?) {
    value?.let {
        setText(value.toString())
    }
}

@InverseBindingAdapter(attribute = "bind:doubleToString")
fun TextInputEditText.getDoubleFromBinding(): Double? {
    val result=text.toString()
    return result.toDoubleOrNull()
}

@BindingAdapter(value = ["doubleToStringAttrChanged"])
fun setListener(view: TextInputEditText, textAttrChanged: InverseBindingListener?) {
    if (textAttrChanged != null) {
        view.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                textAttrChanged.onChange()
            }
        })
    }
}

@BindingAdapter("bind:quantityType")
fun RadioGroup.bindQuantityType(selectedButtonId: QuantityType) {
    when (selectedButtonId) {
        QuantityType.WEIGHT -> check(R.id.kg_radio_button)
        QuantityType.UNIT -> check(R.id.unit_radio_button)
    }
}

@InverseBindingAdapter(attribute = "bind:quantityType")
fun RadioGroup.getQuantityType(): QuantityType {
    return when (checkedRadioButtonId) {
        R.id.kg_radio_button -> QuantityType.WEIGHT
        R.id.unit_radio_button -> QuantityType.UNIT
        else -> QuantityType.UNIT
    }
}

@BindingAdapter(value = ["quantityTypeAttrChanged"])
fun setListener(view: RadioGroup, typeAttrChanged: InverseBindingListener?) {
    if (typeAttrChanged != null) {
        view.setOnCheckedChangeListener { group, checkedId ->
            typeAttrChanged.onChange()
        }
    }
}

@BindingAdapter("bind:decimals")
fun TextInputEditText.setDecimals(decimals: Int) {
    val pattern: Pattern = Pattern.compile(
        "[0-9]{0,9}+((\\.[0-9]{0," + (decimals - 1) + "})?)||(\\.)?"
    )
    filters = arrayOf(InputFilter { _, _, _, dest, _, _ ->
        val matcher: Matcher = pattern.matcher(dest)
        if (!matcher.matches()) "" else null
    })
}
