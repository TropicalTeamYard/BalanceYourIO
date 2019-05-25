package tty.balanceyourio.util

object AmountConvert
{
    fun isBill(str:String):Boolean{
        val d = str.toDoubleOrNull()
        if (d != null){
            if (d <= 999999.99){
               if (str.contains('.')){
                   val r = str.split('.')[1]
                   if (r.length <= 2)
                    return true
                }
                else{
                   return true
               }
            }
        }
        return false
    }
}