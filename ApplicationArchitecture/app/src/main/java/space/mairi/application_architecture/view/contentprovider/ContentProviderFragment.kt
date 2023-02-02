package space.mairi.application_architecture.view.contentprovider

import androidx.appcompat.app.AlertDialog
import android.Manifest.permission
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.View.AUTOFILL_HINT_PHONE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import space.mairi.application_architecture.R
import space.mairi.application_architecture.databinding.FragmentContentProviderBinding

const val REQUEST_CODE = 42

class ContentProviderFragment : Fragment(){
    private var _binding : FragmentContentProviderBinding? = null
    private val binding get() = _binding!!


    companion object {
        @JvmStatic
        fun newInstance() =
            ContentProviderFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkPermission()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addView(context: Context, textToShow: String, textToShowNumb: String?) {
        binding.containerForContacts.addView(AppCompatTextView(context).apply {
            text = textToShow
            textSize = resources.getDimension(R.dimen.main_container_text_size)
        })
        binding.containerForContacts.addView(TextView(context).apply {
            autoLinkMask
            text = "phone" + textToShowNumb
        })
    }

    private fun checkPermission() {
        context?.let {

            when {
                ContextCompat.checkSelfPermission(it, permission.READ_CONTACTS) ==
                        PackageManager.PERMISSION_GRANTED -> {
                            getContacts()
                        }

                shouldShowRequestPermissionRationale(permission.READ_CONTACTS) -> {
                    AlertDialog.Builder(it)
                        .setTitle("Доступ к контактам")
                        .setMessage("Bla bla Объяснение зачем")
                        .setPositiveButton("Предоставить доступ") { _, _ ->
                            requestPermission()
                        }
                        .setNegativeButton("Не надо") { dialog, _ -> dialog.dismiss() }
                        .create()
                        .show()
                }
                else -> {
                    requestPermission()
                }
            }
        }
    }

    private fun requestPermission() {
        requestPermissions(arrayOf(permission.READ_CONTACTS), REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContacts()
                } else {
                    context?.let {
                        AlertDialog.Builder(it)
                            .setTitle("Доступ к контактам")
                            .setMessage("Объяснение")
                            .setNegativeButton("Закрыть") { dialog, _ -> dialog.dismiss() }
                            .create()
                            .show()
                    }
                }
            }
        }
    }

    private fun getContacts() {
        context?.let {
            val contentResolverNumber : ContentResolver = it. contentResolver

                val cursorWithContractDataKinds : Cursor? = contentResolverNumber.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME),
                    null,
                    null,
                    null,
                )


                cursorWithContractDataKinds?.let { cursor ->
                    for (i in 0..cursor.count) {
                        val phone = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                        val pos = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

                        if (cursor.moveToPosition(i)) {
                            val name = cursor.getString(pos)
                            val number = cursor.getString(phone)

                            addView(it, name, number)
                        }

                    }

                }
                cursorWithContractDataKinds?.close()
            }
        }
    }