package space.mairi.application_architecture.test.threads

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.fragment_threads.*
import space.mairi.application_architecture.R
import space.mairi.application_architecture.databinding.FragmentThreadsBinding
import java.util.*
import java.util.concurrent.TimeUnit

const val TEST_BROADCAST_INTENT_FILTER = "TEST BROADCAST INTENT FILTER"
const val THREADS_FRAGMENT_BROADCAST_EXTRA = "THREADS_FRAGMENT_EXTRA"

class ThreadsFragment : Fragment() {
    private var _binding: FragmentThreadsBinding? = null
    private val binding get() = _binding!!

    private var counterThread = 0

    private val testReceiver : BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context : Context, intent : Intent) {

            intent.getStringExtra(THREADS_FRAGMENT_BROADCAST_EXTRA)?.let {
                addView(context, it)
            }
        }
    }

    companion object {
        fun newInstance() = ThreadsFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(testReceiver,
            IntentFilter(TEST_BROADCAST_INTENT_FILTER))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentThreadsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()

        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(testReceiver)
        }

        _binding = null

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtonMainThread()
        initButtonWorkerThread()
        initButtonHandlerThread()
        initServiceWithBroadcastButton()
        initServiceButton()
    }

    private fun initButtonMainThread() {
        binding.button.setOnClickListener {
            binding.textView.text = startCalculations(editText.text.toString().toInt())
            addView(it.context, getString(R.string.in_main_thread))
        }
    }

    private fun initButtonWorkerThread() {
        binding.calcThreadBtn.setOnClickListener {
            Thread{
                counterThread++
                val calculatedText = startCalculations(editText.text.toString().toInt())

                activity?.runOnUiThread{
                    binding.textView.text = calculatedText
                    addView(
                        it.context,
                        String.format("Из потока %d", counterThread)
                    )
                }
            }.start()
        }
    }

    private fun initButtonHandlerThread() {
        val handelerThread = HandlerThread("My HT")
        handelerThread.start()

        val handler = Handler(handelerThread.looper)

        binding.calcThreadHandler.setOnClickListener {
            addView(
                it.context,
                String.format(getString(R.string.in_thread), handelerThread.name)
            )

            handler.post {
                startCalculations(binding.editText.text.toString().toInt())

                mainContainer.post {
                    addView(
                        it.context,
                        String.format(getString(R.string.in_thread), Thread.currentThread().name)
                    )
                }
            }

            handelerThread.quitSafely()
        }
    }

    private fun initServiceWithBroadcastButton() {
        binding.serviceWithBroadcastButton.setOnClickListener {
            context?.let {
                it.startService(Intent(it, MainService::class.java).apply {
                    putExtra(
                        MAIN_SERVICE_INT_EXTRA,
                        binding.editText.text.toString().toInt()
                    )
                })
            }
        }
    }

    private fun initServiceButton() {
        binding.serviceButton.setOnClickListener {
            context?.let {
                it.startService(Intent(it, MainService::class.java).apply {
                    putExtra(
                        MAIN_SERVICE_STRING_EXTRA,
                        getString(R.string.hello_from_thread_fragment)
                    )
                })
            }
        }
    }

    private fun startCalculations(seconds: Int): String {
        val date = Date()
        var diffInSec: Long

        do {
            val currentDate = Date()
            val diffInMs: Long = currentDate.time - date.time
            diffInSec = TimeUnit.MILLISECONDS.toSeconds(diffInMs)
        } while (diffInSec < seconds)

        return diffInSec.toString()
    }

    private fun addView(context: Context, textToShow: String) {
        binding.mainContainer.addView(AppCompatTextView(context).apply {
            text = textToShow
            textSize = resources.getDimension(R.dimen.main_container_text_size)
        })
    }

}