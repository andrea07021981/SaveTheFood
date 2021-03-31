package com.example.savethefood.util

import android.os.Build
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.widget.*
import android.widget.AdapterView.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
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
import com.example.savethefood.constants.QuantityType
import com.example.savethefood.constants.StorageType
import com.example.savethefood.data.domain.FoodDomain
import com.example.savethefood.data.domain.FoodItem
import com.example.savethefood.data.domain.ProductDomain
import com.example.savethefood.food.FoodSearchAdapter
import com.example.savethefood.fooddetail.FoodPantryAdapter
import com.example.savethefood.home.FoodAdapter
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.abs


object FoodBindingUtils {

    /**
     * Needs to be used with [NumberOfSetsConverters.setArrayToString].
     */
    @JvmStatic
    @BindingAdapter("bind:numberOfSets")
    fun setNumberOfSets(view: EditText, value: String) {
        view.setText(value)
    }

    @JvmStatic
    @BindingAdapter("bind:listFoods")
    fun bindRecycleView(recyclerView: RecyclerView, data: List<ProductDomain>?) {
        val adapter = recyclerView.adapter as FoodSearchAdapter
        adapter.submitList(data)
    }
    
    /**
     * Uses the Glide library to load an image by URL into an [ImageView]
     */
    @JvmStatic
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

    @JvmStatic
    @BindingAdapter("bind:htmlConverter")
    fun TextView.bindFoodDescription(html: String?) {//TODO check if we can directly bind in xml with binding exp https://developer.android.com/topic/libraries/data-binding/expressions
        html?.let { text = HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY); }
    }

    @JvmStatic
    @BindingAdapter("bind:foodListData")
    fun bindFoodListData(recyclerView: RecyclerView, data: Collection<FoodItem>?) {
        data?.let {
            val adapter = recyclerView.adapter as FoodTypeAdapter
            adapter.submitList(data.toList())
        }
    }

    @JvmStatic
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

    @JvmStatic
    @BindingAdapter("bind:doubleToString")
    fun TextInputEditText.bindTextDouble(value: Double?) {
        value?.let {
            setText(it.toString())
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "bind:doubleToString")
    fun TextInputEditText.getDoubleFromBinding(): Double? {
        val result=text.toString()
        return result.toDoubleOrNull()
    }

    @JvmStatic
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

    @JvmStatic
    @BindingAdapter("bind:quantityType")
    fun RadioGroup.bindQuantityType(selectedButtonId: QuantityType?) {
        when (selectedButtonId) {
            QuantityType.WEIGHT -> check(R.id.kg_radio_button)
            QuantityType.UNIT -> check(R.id.unit_radio_button)
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "bind:quantityType")
    fun RadioGroup.getQuantityType(): QuantityType {
        return when (checkedRadioButtonId) {
            R.id.kg_radio_button -> QuantityType.WEIGHT
            R.id.unit_radio_button -> QuantityType.UNIT
            else -> QuantityType.UNIT
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["quantityTypeAttrChanged"])
    fun setListener(view: RadioGroup, typeAttrChanged: InverseBindingListener?) {
        if (typeAttrChanged != null) {
            view.setOnCheckedChangeListener { group, checkedId ->
                typeAttrChanged.onChange()
            }
        }
    }

    @JvmStatic
    @BindingAdapter("bind:storageType")
    fun RadioGroup.bindStorageType(selectedButtonId: StorageType?) {
        when (selectedButtonId) {
            StorageType.FRIDGE -> check(R.id.fridge_button)
            StorageType.FREEZER -> check(R.id.freeze_button)
            StorageType.DRY -> check(R.id.dry_button)
            else -> Unit
        }
    }

    @JvmStatic
    @InverseBindingAdapter(attribute = "bind:storageType")
    fun RadioGroup.getStorageType(): StorageType {
        return when (checkedRadioButtonId) {
            R.id.fridge_button -> StorageType.FRIDGE
            R.id.freeze_button -> StorageType.FREEZER
            R.id.dry_button -> StorageType.DRY
            else -> throw Exception("Invalid value")
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["storageTypeAttrChanged"])
    fun setStorageListener(view: RadioGroup, typeAttrChanged: InverseBindingListener?) {
        if (typeAttrChanged != null) {
            view.setOnCheckedChangeListener { _, _ ->
                typeAttrChanged.onChange()
            }
        }
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter("bind:dateToString")
    fun TextInputEditText.bindTextDate(value: Date?) {
        value?.let {
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(value)
            setText(formattedDate)
        } ?: setText("")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @JvmStatic
    @InverseBindingAdapter(attribute = "bind:dateToString")
    fun TextInputEditText.getDateFromBinding(): Date? {
        return try {
            val result = LocalDate.parse(text, DateTimeFormatter.ISO_DATE)
            Date.from(result.atStartOfDay(ZoneId.systemDefault()).toInstant());
        } catch (ex: DateTimeParseException) {
            null
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["dateToStringAttrChanged"])
    fun setDateListener(view: TextInputEditText, textAttrChanged: InverseBindingListener?) {
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

    @JvmStatic
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

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter("bind:formatExpiringDate")
    fun TextView.bindFormatExpiringDate(date: Date) {
        val currentDate = LocalDate.now()
        val oldDate = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        val diff = ChronoUnit.DAYS.between(currentDate, oldDate)

        when {
            diff in 0..3 -> {
                text = context.getString(R.string.expiring_date, diff)
                setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_light))
            }
            diff < 0 -> {
                text = context.getString(R.string.expired, abs(diff))
                setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
            }
            else -> {
                text = context.getString(R.string.expiring_date, diff)
                setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
            }
        }
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter("bind:formatExpiringLabel")
    fun TextView.bindFormatExpiringLabel(date: Date) {
        val currentDate = LocalDate.now()
        val oldDate = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        val diff = ChronoUnit.DAYS.between(currentDate, oldDate)

        when {
            diff in 0..3 -> {
                text = context.getString(R.string.expiring_info)
                setTextColor(ContextCompat.getColor(context, android.R.color.holo_orange_light))
            }
            diff < 0 -> {
                text = context.getString(R.string.expired_info)
                setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_light))
            }
            else -> {
                text = context.getString(R.string.expiring_info)
                setTextColor(ContextCompat.getColor(context, R.color.colorAccent))
            }
        }
    }

    @JvmStatic
    @RequiresApi(Build.VERSION_CODES.O)
    @BindingAdapter("bind:daysIn")
    fun TextView.bindDaysIn(date: Date) {
        val currentDate = LocalDate.now()
        val lastUpdate = date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
        val diff = ChronoUnit.DAYS.between(currentDate, lastUpdate)
        text = context.resources.getQuantityString(R.plurals.days, abs(diff).toInt(), abs(diff).toInt())
    }

    // TODO multiple binding list adapter, find a generic one with a when condition
    @JvmStatic
    @BindingAdapter("bind:listData")
    fun bindFoodRecycleView(recyclerView: RecyclerView, data: List<FoodDomain>?) {
        val adapter = recyclerView.adapter as FoodPantryAdapter
        adapter.submitList(data)
        recyclerView.scheduleLayoutAnimation();
    }
}
