package com.example.npsapiproject

object RegistrationUtil {

    var existingUsers = listOf("")
    var takenEmails = listOf("")
    var validDomains = listOf("@gmail.com")


    //make sure username doesnt match other username in backendless
    //make sure username is at least 3 letters long
    fun validateUsername(username: String) : Boolean {
        var isUsernameTaken = false
        for (i in existingUsers.indices) {
            if (username.equals(existingUsers[i]))
                isUsernameTaken = true
        }
        if (username.length >= 3 && isUsernameTaken != true && username != "")
            return true
        return false
    }


    //make sure password isn't empty
    //make sure password has one digit
    //make sure password has one capital
    //make sure both passwords match
    //make sure password has at least 8 chars

    fun validatePassword(password : String, confirmPassword: String) : Boolean {
        var oneDigit = false
        var oneCapital = true
        var i = 0
        var j = 0
        while (i < password.length) {
            while (j <= 9) {
                if (password.substring(i, i + 1) == ("" + j)) {
                    oneDigit = true
                    j = 10
                    i = password.length
                }
                j++
            }
            i++
            j = 0
        }

        if(password.lowercase() == password)
            oneCapital = false

        if (oneDigit == true && oneCapital == true && password == confirmPassword
            && password.length >= 8 && password != "" && confirmPassword != "")
            return true
        return false
    }

    fun validateEmail(email: String) : Boolean {
        var isEmailTaken = false
        var validEmail = false
        for (i in takenEmails.indices) {
            if (email.equals(takenEmails[i]))
                isEmailTaken = true
        }

        for (i in validDomains.indices) {
            if (email.contains(validDomains[i]))
                validEmail = true
        }

        if (isEmailTaken != true && validEmail == true && email != "")
            return true
        return false
    }


}