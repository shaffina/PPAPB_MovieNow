package com.example.movienow

class DataClass {
    var dataTitle: String? = null
        private set
    var dataDesc: String? = null
        private set
    var dataImage: String? = null
        private set
    var key: String? = null

    constructor(dataTitle: String?, dataDesc: String?, dataImage: String?) {
        this.dataTitle = dataTitle
        this.dataDesc = dataDesc
        this.dataImage = dataImage
    }

    constructor()
}