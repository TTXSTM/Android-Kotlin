package space.mairi.application_architecture.repository.contentprovider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

import space.mairi.application_architecture.R
import space.mairi.application_architecture.room.HistoryDao
import space.mairi.application_architecture.app.App.Companion.getHistoryDao
import space.mairi.application_architecture.room.HistoryEntity
import space.mairi.application_architecture.room.*

private const val URI_ALL = 1
private const val URI_ID = 2
private const val ENTITY_PATH = "HistoryEntity"

class EducationContentProvider : ContentProvider() {

    private var authorities: String? = null

    private lateinit var uriMatcher: UriMatcher

    private var entityContentType: String? = null
    private var entityContentItemType: String? = null

    private lateinit var contentUri: Uri

    override fun getType(uri: Uri): String? {
        when (uriMatcher.match(uri)) {
            URI_ALL -> return entityContentType
            URI_ID -> return entityContentItemType
        }

        return null
    }

    override fun onCreate(): Boolean {
        authorities = context?.resources?.getString(R.string.authorities)

        uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

//        "content:com.geekbrains.androidkotlinapp.provider/HistoryEntity"
        uriMatcher.addURI(authorities, ENTITY_PATH, URI_ALL)

        uriMatcher.addURI(authorities, "$ENTITY_PATH/#", URI_ID)

        entityContentType = "vnd.android.cursor.dir/vnd.$authorities.$ENTITY_PATH"
        entityContentItemType = "vnd.android.cursor.item/vnd.$authorities.$ENTITY_PATH"

        contentUri = Uri.parse("content://$authorities/$ENTITY_PATH")

        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val historyDao : HistoryDao = getHistoryDao()

        val cursor = when (uriMatcher.match(uri)) {
            URI_ALL -> historyDao.getHistoryCursor()
            URI_ID -> {
                val id = ContentUris.parseId(uri)
                historyDao.getHistoryCursor(id)
            }
            else -> throw IllegalArgumentException("Wrong URI: $uri")
        }

        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {

        require(uriMatcher.match(uri) == URI_ALL) { "Wrong URI: $uri" }

        val historyDao = getHistoryDao()

        val entity = map(values)
        val id : Long = entity.id

        historyDao.insert(entity)

        val resultUri = ContentUris.withAppendedId(contentUri, id )

        context?.contentResolver?.notifyChange(resultUri, null)

        return resultUri
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        require(uriMatcher.match(uri) == URI_ID) { "Wrong URI: $uri" }

        val historyDao = getHistoryDao()
        historyDao.update(map(values))

        context?.contentResolver?.notifyChange(uri, null)

        return 1
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        require(uriMatcher.match(uri) == URI_ID) { "Wrong URI: $uri" }

        val historyDao = getHistoryDao()
        val id = ContentUris.parseId(uri)

        historyDao.deleteById(id)

        context?.contentResolver?.notifyChange(uri, null)

        return 1
    }

    private fun map(values: ContentValues?) : HistoryEntity {
        return if (values == null) {
            HistoryEntity()
        } else {
            val id = if (values.containsKey(ID)) values[ID] as Long else 0

            val city = values[CITY] as String

            val temperature = values[TEMPERATURE] as Int

            HistoryEntity(id, city, temperature)
        }

    }
}