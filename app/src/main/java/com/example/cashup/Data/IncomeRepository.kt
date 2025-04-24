package com.example.cashup.Data


// Singleton object to hold income data in memory
object IncomeRepository {

    // The private list that stores the income items
    private val incomeList = mutableListOf<IncomeItem>()

    // Function to add a new income item to the list
    fun addIncome(income: IncomeItem) {
        incomeList.add(income)

        println("Income added: $income")
        println("Total items in memory: ${incomeList.size}")
    }


    fun getAllIncome(): List<IncomeItem> {
        return incomeList.toList()
    }


    fun clearAllIncome() {
        incomeList.clear()
        println("In-memory income data cleared.")
    }
}
