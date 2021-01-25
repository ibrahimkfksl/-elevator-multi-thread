# -elevator-multi-thread
elevator simulation with java



	-Proje Hakkında
-Bu proje thread'leri anlamak ve bunları kullanmak amacı ile yazılmıştır. Bir alışveriş merkezinde bulunan beş adet asansörün kontrolünü sağlamak amaçlanmaktadır. Asansörü bekleyen kişi sayısı göz önünde bulundurularak
 asansörler durdurulup çalıştırılabiliyor. Biz ise bu asansörleri hangi şartlarda nasıl hareketlerde bulunacağına karar veriyoruz.
	
	-Proje Nasıl Çalıştırılır
-Proje için arayüz geliştirilmediği için bir IDE vasıtasıyla çalıştırılmalıdır.
-Proje IDE de çalıştırıldığında konsol ekranında 1 saniye aralıklarla projede kullanılan threadlerin bilgilerini ekrana yazacaktır.

	-Uyarı
-Proje çalıştığında konsol ekranına yazdırılan bilgilerde tutarsızlık var gibi gözükebilir ancak tutarsızlık yoktur.
 Sebebi ise bir threadlerden kaynaklanıyor. Mesela giriş katında bulunan müşterilerin kuyruktaki bilgileri yazdırılıyor.
 Daha sonra asansorlerden biri gelip buradan müşteriyi alıyor. Daha sonra da asansörlerin bilgileri yazdırılırken kuyrukta gözüken müşteri
 aynı zamanda da asansördeymiş gibi gözüküyor. Yani veriler tutarsız gibi gözüksede tamamen threadlerden kaynaklanan bir bilgi karmaşası oluyor.
 
  -Projeyi Geliştirirken Kullandığımız Teknoloji
-Proje de Java Programlama dili kullanılmıştır.
-Proje Eclipse IDE si kullanılmıştır.

	-Projeyi Geliştirenler
Abdullah Yaşar KISA
İbrahim KAFKASLI
