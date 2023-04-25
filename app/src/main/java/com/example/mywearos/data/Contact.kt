package com.example.mywearos.data

data class Contact(val firstname: String, val lastname: String, val number: String? = null)

object ContactRepo{
    fun getContacts() = contacts.sortedBy { it.lastname  }

    fun getContactsSeparatedByFirstLetter(): List<List<Contact>>{
        val allContactsSortedByLastname = contacts.sortedBy { it.lastname  }
        val seperatedContacts = mutableListOf<List<Contact>>()
        var currentLetterGroup = mutableListOf<Contact>()
        for(contact in allContactsSortedByLastname){
            if(currentLetterGroup.isEmpty()){
                currentLetterGroup.add(contact)
            }else
                if(currentLetterGroup.first().lastname[0] == contact.lastname[0]){
                    currentLetterGroup.add(contact)
                }else{
                    seperatedContacts.add(currentLetterGroup)
                    currentLetterGroup = mutableListOf<Contact>(contact)
                }
        }
        return seperatedContacts
    }
}

private val contacts = listOf(
    Contact("Lena", "Müller", "0151-1234567"),
    Contact("Max", "Schmidt", "0172-2345678"),
    Contact("Julia", "Schneider", "0160-3456789"),
    Contact("Simon", "Klein", "0157-4567890"),
    Contact("Sophie", "Bauer", "0176-5678901"),
    Contact("Paul", "Hoffmann", "0152-6789012"),
    Contact("Hannah", "Schulz", "0163-7890123"),
    Contact("Felix", "Wagner", "0171-8901234"),
    Contact("Mia", "Becker", "0159-0123456"),
    Contact("David", "Herrmann", "0162-1234567"),
    Contact("Lea", "Krause", "0173-2345678"),
    Contact("Nico", "Huber", "0156-3456789"),
    Contact("Laura", "Richter", "0175-4567890"),
    Contact("Alexander", "Koch", "0153-5678901"),
    Contact("Sarah", "Schmitt", "0161-6789012"),
    Contact("Tobias", "Bergmann", "0174-7890123"),
    Contact("Lisa", "Engel", "0158-8901234"),
    Contact("Marc", "Schulze", "0164-0123456"),
    Contact("Vanessa", "Neumann", "0177-1234567"),
    Contact("Jan", "Kuhn", "0155-2345678"),
    Contact("Lara", "Vogel", "0178-3456789"),
    Contact("Dominik", "Franz", "0165-4567890"),
    Contact("Anna", "Krüger", "0154-5678901"),
    Contact("Martin", "Werner", "0170-6789012"),
    Contact("Carina", "Hansen", "0166-7890123"),
    Contact("Sebastian", "Schröder", "0179-8901234"),
    Contact("Jana", "Baumann", "0150-0123456"),
    Contact("Benjamin", "Peters", "0167-1234567"),
    Contact("Emily", "Kaiser", "0176-2345678"),
    Contact("Kevin", "Meyer", "0157-3456789"),
    Contact("Laura", "Hoffmann", "0172-4567890"),
    Contact("Nils", "Schmitz", "0151-5678901"),
    Contact("Sophia", "Lange", "0160-6789012"),
    Contact("Julian", "Roth", "0173-7890123"),
    Contact("Leah", "Sauer", "0152-8901234"),
    Contact("Philipp", "Kramer", "0163-0123456"),
    Contact("Maren", "Voigt", "0171-1234567"),
    Contact("Markus", "Weber", "0159-2345678"),
    Contact("Melanie", "Böhme", "0172-3456789"),
    Contact("Robin", "Schultz", "0161-4567890"),
    Contact("Lena", "Schmidt", "0151-12345678"),
    Contact("Tim", "Müller", "0176-23456789"),
    Contact("Julia", "Koch", "0152-34567890"),
    Contact("Johannes", "Schneider", "0171-45678901"),
    Contact("Katharina", "Wagner", "0157-56789012"),
    Contact("Daniel", "Schulz", "0172-67890123"),
    Contact("Lisa", "Klein", "0159-78901234"),
    Contact("Andreas", "Bauer", "0173-89012345"),
    Contact("Sarah", "Becker", "0154-90123456"),
    Contact("David", "Hoffmann", "0175-01234567"),
    Contact("Anna", "Richter", "0156-12345678"),
    Contact("Matthias", "Krüger", "0177-23456789"),
    Contact("Sophie", "Neumann", "0153-34567890"),
    Contact("Martin", "Huber", "0170-45678901"),
    Contact("Caroline", "Krause", "0158-56789012"),
    Contact("Thomas", "Meier", "0174-67890123"),
    Contact("Laura", "Böhm", "0159-78901234"),
    Contact("Christian", "Schneider", "0171-89012345"),
    Contact("Maria", "Schwarz", "0154-90123456"),
    Contact("Simon", "Walter", "0176-01234567"),
    Contact("Sabine", "Petersen", "0157-12345678"),
    Contact("Tobias", "Lange", "0172-23456789"),
    Contact("Nina", "Baumann", "0152-34567890"),
    Contact("Marco", "Kuhn", "0175-45678901"),
    Contact("Lea", "Hansen", "0156-56789012"),
    Contact("Jan", "Fischer", "0173-67890123"),
    Contact("Marie", "Engel", "0158-78901234"),
    Contact("Maximilian", "Kaiser", "0170-89012345"),
    Contact("Franziska", "Schmitt", "0153-90123456"),
    Contact("Peter", "Berger", "0177-01234567"),
    Contact("Laura", "Hermann", "0154-12345678"),
    Contact("Benjamin", "Ludwig", "0176-23456789"),
    Contact("Sophia", "Simon", "0152-34567890"),
    Contact("Florian", "Schubert", "0171-45678901"),
    Contact("Jana", "Bayer", "0157-56789012"),
    Contact("Robert", "Schulze", "0172-67890123"),
    Contact("Hannah", "Keller", "0159-78901234"),
    Contact("Markus", "Kraft", "0173-89012345"),
    Contact("Sarah", "Möller", "0154-90123456")
)
