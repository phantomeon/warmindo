package com.android.warmindo

class TransactionModel(
    var idpelanggan: String? = null,
    var idpengguna: String? = null,
    var idpromosi: String? = null,
    var idtransaksi: String? = null,
    var kodemeja: String? = null,
    var metodepembayaran: String? = null,
    var namapelanggan: String? = null,
    var status: String? = null,
    var tanggal: String? = null,
    var waktu: String? = null,
    var shift: Int? = null,
    var total: Int? = null,
    var totaldiskon: Int? = null
) {
    fun getNoTransaksi(): String? {
        return idtransaksi?.takeLast(4)
    }
}
