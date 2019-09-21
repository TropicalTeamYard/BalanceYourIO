package tty.balanceyourio.model

class Tuple<T1, T2, T3>(item1: T1, item2: T2, item3: T3) {

    var item1: T1 = item1
        private set
    var item2: T2 = item2
        private set
    var item3: T3 = item3
        private set
}

class GroupDateTuple<Int, Date>(position: Int?, date: Date?) {
    var position: Int? = position
        set(value) {
            if (value == null && date != null) {
                date = null
            }
            field = value
        }

    var date: Date? = date
        set(value) {
            if (value == null && position != null) {
                position = null
            }
            field = value
        }
}
