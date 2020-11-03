package edu.isel.pdm.memorymatrix

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel

class MatrixState(var toGuess: MatrixPattern?, var current: MatrixPattern?, var side: Int) : Parcelable {
    constructor(parcel: Parcel) : this(null, null, 0) {
        this.side = parcel.readInt()
        val toGuess = mutableListOf<Position>()
        parcel.readTypedList(toGuess, Position.CREATOR)
        val current = mutableListOf<Position>()
        parcel.readTypedList(current, Position.CREATOR)
        this.toGuess = MatrixPattern(toGuess, side)
        this.current = MatrixPattern(current, side)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(side)
        parcel.writeList(toGuess?.toList())
        parcel.writeList(current?.toList())
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MatrixState> {
        override fun createFromParcel(parcel: Parcel): MatrixState {
            return MatrixState(parcel)
        }

        override fun newArray(size: Int): Array<MatrixState?> {
            return arrayOfNulls(size)
        }
    }

}

class MatrixViewModel(
    var toGuess: MatrixPattern? = null,
    var current: MatrixPattern? = null
) : ViewModel() {
    fun startGame(guessCount: Int, matrixSide: Int): MatrixViewModel {
        toGuess = MatrixPattern.fromRandom(guessCount, matrixSide)
        current = MatrixPattern.empty(matrixSide)
        return this
    }

    fun isGameOngoing() = toGuess != null && toGuess?.count != current?.count

    fun addGuess(guess: Position): MatrixViewModel {
        current = current?.plus(guess)
        return this
    }

    fun getState() = MatrixState(this.toGuess, this.current, toGuess?.side ?: 0)
}
