# WEB-SERVER-JAVA
untuk pembelajaran beberapa hari ini kita di fokuskan untuk membuat web server dengan bahasa pemrograman java. Dalam web server terdiri dari beberapa file
1.	WebServer.java
2.	Server.java
3.	http_protokol.java

Webserver.java digunakan untuk menampilkan method main yang artinya semua instruksi di dalam fungsi tersebut biasanya digunakan untuk memanggil fungsi lain agar program berjalan sesuai tujuan.
file class server.java digunakan untuk membangun server tertatanam.
File http_protokol dikaitkan dengan file server untuk memproses permintaan. Setiap ada request yang ditemukan  maka akan muncul pesan “ada request dari browser ”  dan apabila data permintaan atau request tidak di temukan ditolak dengan respons “kosong”
Ada dua metode untuk permintaan /request antara klien dan server melalui http_protokol.
	Get – meminta data dari sumber data tertentu
	Post mengirimkan data untuk di proses ke sumber data yang teridentifikasi.
	terdapat beberapa class yang ada pada http protokol
  1. public void handleRequest 
  2. private void handleResponse
  3. private static Path getFilePath
  4. private static String guessContentType
  5. private byte[] callPHP
