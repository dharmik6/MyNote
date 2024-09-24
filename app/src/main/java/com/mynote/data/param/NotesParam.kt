package com.mynote.data.param

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class NotesParam(
    @SerializedName("createdAt")
    var createdAt: String? = null,
    @SerializedName("color")
    var color: String? = null,
    @SerializedName("images")
    var images: List<String?>? = null,
    @SerializedName("items")
    var items: List<Item?>? = null,
    @SerializedName("noteId")
    var noteId: String? = null,
    @SerializedName("noteText")
    var noteText: String? = null,
    @SerializedName("subNotes")
    var subNotes: List<SubNote?>? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("type")
    var type: Int? = null,
    @SerializedName("updatedAt")
    var updatedAt: String? = null,
    @SerializedName("isArchived")
    var isArchived: Boolean = false,
    @SerializedName("isSelected")
var isSelected: Boolean = false
) : Parcelable {

    fun toMap(): Map<String, Any?> {
        return mapOf(
            "createdAt" to createdAt,
            "color" to color,
            "images" to images,
            "items" to items?.map { it?.toMap() },  // Check if 'items' field is null
            "noteId" to noteId,
            "noteText" to noteText,
            "subNotes" to subNotes?.map { it?.toMap() },  // Check if 'subNotes' field is null
            "title" to title,
            "type" to type,
            "updatedAt" to updatedAt,
            "archived" to isArchived // Include in map
        )
    }

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.createTypedArrayList(Item),
        parcel.readString(),
        parcel.readString(),
        parcel.createTypedArrayList(SubNote),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
      isArchived = parcel.readByte() != 0.toByte() // Read isArchived as a Boolean
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(createdAt)
        parcel.writeString(color)
        parcel.writeStringList(images)
        parcel.writeTypedList(items)
        parcel.writeString(noteId)
        parcel.writeString(noteText)
        parcel.writeTypedList(subNotes)
        parcel.writeString(title)
        parcel.writeValue(type)
        parcel.writeString(updatedAt)
        parcel.writeByte(if (isArchived) 1 else 0) // Write isArchived as a Boolean
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NotesParam> {
        override fun createFromParcel(parcel: Parcel): NotesParam {
            return NotesParam(parcel)
        }

        override fun newArray(size: Int): Array<NotesParam?> {
            return arrayOfNulls(size)
        }
    }

    data class Item(
        @SerializedName("isChecked")
        var isChecked: Boolean = false,  // Change from non-nullable Boolean to nullable Boolean
        @SerializedName("subItems")
        var subItems: List<SubItem?>? = null,
        @SerializedName("name")
        var name: String? = null
    ) : Parcelable {

        fun toMap(): Map<String, Any?> {
            return mapOf(
                "checked" to isChecked,
                "subItems" to subItems?.map { it?.toMap() },  // Convert SubItem to map
                "name" to name
            )
        }

        constructor(parcel: Parcel) : this(
            parcel.readValue(Boolean::class.java.classLoader) as Boolean,  // Read as nullable Boolean
            parcel.createTypedArrayList(SubItem),
            parcel.readString()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(isChecked)  // Write nullable Boolean
            parcel.writeTypedList(subItems)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Item> {
            override fun createFromParcel(parcel: Parcel): Item {
                return Item(parcel)
            }

            override fun newArray(size: Int): Array<Item?> {
                return arrayOfNulls(size)
            }
        }



    data class SubItem(
            @SerializedName("isChecked")
            var isChecked: Boolean = false,
            @SerializedName("name")
            var name: String? = null
        ) : Parcelable {

            fun toMap(): Map<String, Any?> {
                return mapOf(
                    "checked" to isChecked,
                    "name" to name
                )
            }

            constructor(parcel: Parcel) : this(
                parcel.readValue(Boolean::class.java.classLoader) as Boolean,
                parcel.readString()
            )

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeValue(isChecked)
                parcel.writeString(name)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<SubItem> {
                override fun createFromParcel(parcel: Parcel): SubItem {
                    return SubItem(parcel)
                }

                override fun newArray(size: Int): Array<SubItem?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    data class SubNote(
        @SerializedName("isChecked")
        var isChecked: Boolean = false,
        @SerializedName("noteText")
        var noteText: String? = null,
        @SerializedName("title")
        var title: String? = null
    ) : Parcelable {

        fun toMap(): Map<String, Any?> {
            return mapOf(
                "checked" to isChecked,
                "noteTextF" to noteText,
                "title" to title
            )
        }

        constructor(parcel: Parcel) : this(
            parcel.readValue(Boolean::class.java.classLoader) as Boolean,
            parcel.readString(),
            parcel.readString()
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeValue(isChecked)
            parcel.writeString(noteText)
            parcel.writeString(title)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<SubNote> {
            override fun createFromParcel(parcel: Parcel): SubNote {
                return SubNote(parcel)
            }

            override fun newArray(size: Int): Array<SubNote?> {
                return arrayOfNulls(size)
            }
        }
    }
}
