package com.example.savethefood.home

import android.animation.Animator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.transition.TransitionInflater
import com.example.savethefood.EventObserver
import com.example.savethefood.MainActivity
import com.example.savethefood.R
import com.example.savethefood.data.source.repository.FoodDataRepository
import com.example.savethefood.databinding.FragmentHomeBinding
import com.example.savethefood.fooddetail.FoodDetailViewModel
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import java.lang.reflect.InvocationTargetException

class HomeFragment : Fragment(), View.OnLayoutChangeListener {

    private val homeViewModel by viewModels<HomeViewModel>{
        HomeViewModel.HomeViewModelFactory(FoodDataRepository.getRepository(requireActivity().application))
    }

    private val args: HomeFragmentArgs by navArgs()
    private lateinit var dataBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View? {
        dataBinding = FragmentHomeBinding.inflate(inflater)
        dataBinding.lifecycleOwner = this
        dataBinding.homeViewModel = homeViewModel
        dataBinding.foodRecycleview.layoutManager = GridLayoutManager(activity, 2)
        setHasOptionsMenu(true)
        dataBinding.rootLayout.addOnLayoutChangeListener(this)
        return dataBinding.root
    }

    /*
     * Called when the fragment's activity has been created and this
     * fragment's view hierarchy instantiated.  It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     */

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dataBinding.foodRecycleview.adapter =
            FoodAdapter(FoodAdapter.OnClickListener {
                homeViewModel.moveToFoodDetail(it)
            })

        homeViewModel.detailFoodEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                val foodImageView =
                    dataBinding.foodRecycleview.findViewById<ImageView>(R.id.food_imageview)
                val foodTextview =
                    dataBinding.foodRecycleview.findViewById<TextView>(R.id.food_textview)
                val extras = FragmentNavigatorExtras(
                    foodImageView to "foodImage",
                    foodTextview to "foodTitle")
                val bundle = bundleOf("foodDomain" to it)
                findNavController()
                    .navigate(R.id.foodDetailFragment, bundle, null, extras)
            }
        })

        homeViewModel.barcodeFoodEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                FragmentIntentIntegrator(this).initiateScan()
            }
        })

        homeViewModel.onlineFoodEvent.observe(this.viewLifecycleOwner, EventObserver {
            it.let {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToFoodFragment())
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        //TODO forced value, emulator can't read barcode
        //041631000564
        result?.let {
            homeViewModel.getApiFoodDetails(it.contents ?: "041631000564")
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    inner class FragmentIntentIntegrator(
        private val fragment: Fragment
    ) : IntentIntegrator(fragment.activity) {

        override fun startActivityForResult(intent: Intent, code: Int) {
            fragment.startActivityForResult(intent, code)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return super.onOptionsItemSelected(item)
    }

    /**
     * Reveal animation for the view
     */
    private fun animateTransition(params: Bundle?) {
        dataBinding.lifecycleOwner = this
        // Check if the runtime version is at least Lollipop
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // get the center for the clipping circle
            val cx = dataBinding.rootLayout.width / 2
            val cy = dataBinding.rootLayout.height / 2
            val xPosition = params?.getFloat("x")?.toInt() ?: 0
            val yPosition = params?.getFloat("y")?.toInt() ?: 0

            // get the final radius for the clipping circle
            val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

            // create the animator for this view (the start radius is zero)
            val anim = ViewAnimationUtils.createCircularReveal(
                dataBinding.rootLayout,
                xPosition,
                yPosition,
                0f,
                finalRadius
            )
            // make the view visible and start the animation
            dataBinding.rootLayout.visibility = View.VISIBLE
            anim.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                    //TODO START ANIMATION FOR THE VIEW CHILDREN
                }

            })
            anim.start()
        }
        dataBinding.rootLayout.removeOnLayoutChangeListener(this)
    }

    override fun onLayoutChange(
        p0: View?,
        p1: Int,
        p2: Int,
        p3: Int,
        p4: Int,
        p5: Int,
        p6: Int,
        p7: Int,
        p8: Int
    ) {
        try {
            animateTransition(
                args.params)
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
    }
}