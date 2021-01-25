package Avm;

import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

class Degiskenler {

	/*
	 * Bu classta adindan da anlasildigi gibi kullandigimiz degerler ve bu degerleri
	 * degistirmek icin metodlar bulunuyor.
	 */
	static CopyOnWriteArrayList<Integer> kuyruk_giren = new CopyOnWriteArrayList<Integer>();
	static int[] kuyruk_cikan = new int[5];// cikis yapacak kisi sayisini tutuyor
	static int[] kattaki_musteri = new int[5];// katlarda bulunanlari tutuyor
	static int toplam_kuyruk;

	static Asansor asansor1 = new Asansor(true, "Asansor1");
	static Asansor asansor2 = new Asansor(false, "Asansor2");
	static Asansor asansor3 = new Asansor(false, "Asansor3");
	static Asansor asansor4 = new Asansor(false, "Asansor4");
	static Asansor asansor5 = new Asansor(false, "Asansor5");

	static void baslat() {

		kuyruk_cikan[0] = 0;
		kuyruk_cikan[1] = 0;
		kuyruk_cikan[2] = 0;
		kuyruk_cikan[3] = 0;
		kuyruk_cikan[4] = 0;

		Kontrol kontrol = new Kontrol();
		kontrol.start();
		Login login = new Login();
		login.start();
		Exit exit = new Exit();
		exit.start();
		Cikti cikti = new Cikti();
		cikti.start();

		Degiskenler.asansor1.start();
		Degiskenler.asansor2.start();
		Degiskenler.asansor3.start();
		Degiskenler.asansor4.start();
		Degiskenler.asansor5.start();
	}

	/*
	 * Bu aciklamanin altinda kalan tum metodlar Degiskenler classinin
	 * degiskenlerinin degerlerini degistirmek icin kullaniliyor. Hepsi de
	 * threadlerden kaynakli karmasiklik olmamasi icin synchronized sekilde ve
	 * nesnelerle kiliti sekilde tutuluyor
	 */
	private static Object lock0 = new Object();

	static synchronized void kuyrukGirenYeniElemanEkle(int sayi) {
		synchronized (lock0) {

			Degiskenler.kuyruk_giren.add(sayi);

		}
	}

	private static Object lock1 = new Object();

	static synchronized void kuyrukGirenSayiDusur(int sayi) {
		synchronized (lock1) {
			Degiskenler.kuyruk_giren.set(0, Degiskenler.kuyruk_giren.get(0) - sayi);
		}
	}

	private static Object lock2 = new Object();

	static synchronized void kuyrukGirenElemanSil() {
		synchronized (lock2) {
			if (!Degiskenler.kuyruk_giren.isEmpty()) {
				Degiskenler.kuyruk_giren.remove(1);
				Degiskenler.kuyruk_giren.remove(0);
			}
		}
	}

	private static Object lock3 = new Object();

	static synchronized void kattakiMusterileriEkle(int kat, int sayi) {
		synchronized (lock3) {
			kattaki_musteri[kat] += sayi;
		}
	}

	private static Object lock4 = new Object();

	static synchronized void kattakiMusterileriCikar(int kat, int sayi) {
		synchronized (lock4) {
			kattaki_musteri[kat] -= sayi;
		}
	}

	private static Object lock5 = new Object();

	static synchronized void kuyrukCikanArtir(int kat, int sayi) {
		synchronized (lock5) {
			kuyruk_cikan[kat] += sayi;
		}
	}

	private static Object lock6 = new Object();

	static synchronized void kuyrukCikanAzalt(int kat, int sayi) {
		synchronized (lock6) {
			kuyruk_cikan[kat] -= sayi;
		}
	}

	private static Object lock7 = new Object();

	static synchronized void toplamKuyrukArtir(int sayi) {
		synchronized (lock7) {
			toplam_kuyruk += sayi;
		}
	}

	private static Object lock8 = new Object();

	static synchronized void toplamKuyrukAzalt(int sayi) {
		synchronized (lock8) {
			toplam_kuyruk -= sayi;
		}
	}

}

class Login extends Thread implements Runnable {
	/*
	 * Login classimiz da AVM ye musteri girisini yapan Threadi temsil ediyor ve
	 * gerekli degerleri tutuyor
	 * 
	 */
	Thread login;
	int giren_musteri;
	int hedef_kat;
	Random rand = new Random();

	@Override
	public void run() {
		while (true) {
			giren_musteri = rand.nextInt(9) + 1;
			hedef_kat = rand.nextInt(4) + 1;

			Degiskenler.kuyrukGirenYeniElemanEkle(giren_musteri);
			Degiskenler.kuyrukGirenYeniElemanEkle(hedef_kat);
			Degiskenler.kattakiMusterileriEkle(0, giren_musteri);
			Degiskenler.toplamKuyrukArtir(giren_musteri);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void start() {
		login = new Thread(this, "Login");
		login.start();
	}

}

class Exit implements Runnable {

	/*
	 * Bu classimiz da AVM de katlardan musterilerin rastgele cikisini saglayan
	 * Threadi temsil ediyor.
	 * 
	 */
	Thread exit;
	int cikan_musteri;
	int bulundugu_kat;
	Random rand = new Random();

	@Override
	public void run() {

		while (true) {
			cikan_musteri = rand.nextInt(5) + 1;
			for (int i = 0; i < cikan_musteri; i++) {
				bulundugu_kat = rand.nextInt(4) + 1;

				if (Degiskenler.kattaki_musteri[bulundugu_kat] - 1 < 0) {
					i--;
					continue;
				} else {

					Degiskenler.kuyrukCikanArtir(bulundugu_kat, 1);
					Degiskenler.kattakiMusterileriCikar(bulundugu_kat, 1);
					Degiskenler.toplamKuyrukArtir(1);
				}

			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void start() {
		exit = new Thread(this, "Exit");
		exit.start();
	}

}

class Kontrol extends Thread implements Runnable {

	/*
	 * Bu classda anlik olarak kuyrukta bekleyen musteri sayisini kontrol ederek
	 * asansorun aktif olup olmamasini sagliyor Istenilenin disinda 100ms araliklar
	 * ile kontrol sagliyor
	 * 
	 */
	Thread kontrol;

	@Override
	public void run() {

		try {

			while (true) {

				if (Degiskenler.toplam_kuyruk >= 0 && Degiskenler.toplam_kuyruk < 20) {
					Degiskenler.asansor1.setAktif_mi(true);
					Degiskenler.asansor2.setAktif_mi(false);
					Degiskenler.asansor3.setAktif_mi(false);
					Degiskenler.asansor4.setAktif_mi(false);
					Degiskenler.asansor5.setAktif_mi(false);
				}

				else if (Degiskenler.toplam_kuyruk >= 20 && Degiskenler.toplam_kuyruk < 40) {
					Degiskenler.asansor1.setAktif_mi(true);
					Degiskenler.asansor2.setAktif_mi(true);
					Degiskenler.asansor3.setAktif_mi(false);
					Degiskenler.asansor4.setAktif_mi(false);
					Degiskenler.asansor5.setAktif_mi(false);
				}

				else if (Degiskenler.toplam_kuyruk >= 40 && Degiskenler.toplam_kuyruk < 60) {
					Degiskenler.asansor1.setAktif_mi(true);
					Degiskenler.asansor2.setAktif_mi(true);
					Degiskenler.asansor3.setAktif_mi(true);
					Degiskenler.asansor4.setAktif_mi(false);
					Degiskenler.asansor5.setAktif_mi(false);
				}

				else if (Degiskenler.toplam_kuyruk >= 80 && Degiskenler.toplam_kuyruk < 100) {
					Degiskenler.asansor1.setAktif_mi(true);
					Degiskenler.asansor2.setAktif_mi(true);
					Degiskenler.asansor3.setAktif_mi(true);
					Degiskenler.asansor4.setAktif_mi(true);
					Degiskenler.asansor5.setAktif_mi(false);
				}

				else if (Degiskenler.toplam_kuyruk >= 100) {
					Degiskenler.asansor1.setAktif_mi(true);
					Degiskenler.asansor2.setAktif_mi(true);
					Degiskenler.asansor3.setAktif_mi(true);
					Degiskenler.asansor4.setAktif_mi(true);
					Degiskenler.asansor5.setAktif_mi(true);
				}

				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void start() {
		kontrol = new Thread(this, "Kontrol");
		kontrol.start();
	}
}

class Asansor extends Thread implements Runnable {

	/*
	 * Bu classta bir asansorun tum ozelliklerini tutuyor. Gerekli metodlari ve
	 * degerleri tutuyor Her asansor icin bir nesne olusturulmasi gerekiyor.
	 */
	private boolean aktif_mi;
	private boolean hareket_ediyor_mu;
	private int bulundugu_kat;
	private int hedef_kat;
	private int doluluk;
	private String isim;
	private String yonelim;
	private int[] musterivekat;

	Thread asansor = new Thread(this);

	public Asansor(boolean aktif_mi, String isim) {
		this.aktif_mi = aktif_mi;
		bulundugu_kat = 0;
		hareket_ediyor_mu = false;
		hedef_kat = 0;
		doluluk = 0;
		this.isim = isim;
		musterivekat = new int[5];
		for (int i = 0; i < 5; i++) {
			musterivekat[i] = 0;
		}

	}

	@Override
	public void run() {
		while (true) {
			hareketEt();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Bu metotda asansorun aktifligi ve bulundugu kat kontrol edilerek dogru
	 * sekilde hareket etmesi saglaniyor
	 */
	private void hareketEt() {

		if (aktif_mi) {
			hareket_ediyor_mu = true;
			if (bulundugu_kat == 0) {
				yeniMusteriGirisi();
				yeniMusteriKatlaraTasi();
			} else if (bulundugu_kat != 0) {
				if (!cikisYapacakMusteriVarMi()) {
					if (!girisYapacakMusteriVarMi()) {
						hareket_ediyor_mu = false;
					} else {
						girisKataYonel();
					}
				} else {
					cikisMusterileriniTopla();
				}
			}

		}

		/*
		 * burada karisiklik olmamasi icin eger hareket aninda isi kesilecek olursa diye
		 * isi bittikten sonra hedefi 0. kat yapiyoruz
		 */

		if (!aktif_mi) {
			hedef_kat = 0;
			hareket_ediyor_mu = false;
		}
	}

	/*
	 * Asansor zemin kattan musterileri diger katlara dagitmak icin aliyor
	 */
	static Object lock1 = new Object();

	public void yeniMusteriGirisi() {
		synchronized (lock1) {
			while (true) {
				if (doluluk >= 10 || Degiskenler.kuyruk_giren.isEmpty()) {
					break;
				} else {

					if (Degiskenler.kuyruk_giren.get(0) > 0) {
						Degiskenler.kuyrukGirenSayiDusur(1);
						Degiskenler.toplamKuyrukAzalt(1);
						doluluk++;
						musterivekat[Degiskenler.kuyruk_giren.get(1)]++;
						Degiskenler.kattakiMusterileriCikar(0, 1);
					}
					if (Degiskenler.kuyruk_giren.get(0) <= 0) {
						Degiskenler.kuyrukGirenElemanSil();
					}

				}
			}
		}
	}

	/*
	 * Zemin kattan aldigi musterileri sira ile katlara dagitmaya basliyor Eger bir
	 * katta musteri inmeyecekse o katta durmuyor. Ýcerisindeki musterileri
	 * indirirken yeni musteri almiyor
	 * 
	 * Katlar arasi gecerken gerekli bekletme islem bu metodda kullaniliyor
	 */
	static Object lock2 = new Object();

	public void yeniMusteriKatlaraTasi() {

		try {
			yonelim = "Yukari";
			while (doluluk != 0) {

				for (int i = bulundugu_kat; i <= 4; i++) {
					if (musterivekat[i] > 0) {
						Thread.sleep(200 * (i - bulundugu_kat));
						bulundugu_kat = i;
						break;
					}
				}

				synchronized (lock2) {
					Degiskenler.kattakiMusterileriEkle(bulundugu_kat, musterivekat[bulundugu_kat]);
					doluluk -= musterivekat[bulundugu_kat];
					musterivekat[bulundugu_kat] = 0;
				}

			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Bu metot tum katlari kontrol edip cikis yapacak musteri var mi onu kontrol
	 * ediyor
	 */
	public Boolean cikisYapacakMusteriVarMi() {
		for (int i = 1; i < 5; i++) {
			if (Degiskenler.kuyruk_cikan[i] > 0) {
				return true;
			}
		}

		return false;
	}

	/*
	 * Bu metot giris katini kontrol edip musteri var ise true return yapiyor
	 */
	public Boolean girisYapacakMusteriVarMi() {

		if (!Degiskenler.kuyruk_giren.isEmpty()) {
			return true;

		}

		return false;
	}

	/*
	 * Asansorlerin giris kata gidecegi durumlarda kullaniliyor Bu durumlar
	 * asansorun musterileri indirme durumu ya da giris yapan musterileri alip diger
	 * katlara dagitma durumu
	 */
	public void girisKataYonel() {
		try {
			yonelim = "Asagi";
			hedef_kat = 0;
			Thread.sleep(200 * (bulundugu_kat));
			bulundugu_kat = 0;
			doluluk = 0;
			musterivekat[0] = 0;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Cikis yapacak musterileri cikis yapacagi katlarin arasindan en usteki kattan
	 * baslayarak zemine dogru topluyor
	 */
	public void cikisMusterileriniTopla() {
		try {
			if (Degiskenler.kuyruk_cikan[bulundugu_kat] > 0) {
				while (Degiskenler.kuyruk_cikan[bulundugu_kat] > 0 && doluluk < 10) {
					doluluk++;
					musterivekat[0]++;
					Degiskenler.kuyrukCikanAzalt(bulundugu_kat, 1);
					Degiskenler.toplamKuyrukAzalt(1);
				}

			}

			for (int i = 4; i > 0; i--) {
				if (Degiskenler.kuyruk_cikan[i] > 0) {
					if (digerAsansorHedefleriKarsilastir(i)) {

						hedef_kat = i;
						if (bulundugu_kat - hedef_kat > 0) {
							yonelim = "Asagi";
						} else if (bulundugu_kat - hedef_kat < 0) {
							yonelim = "Yukari";
						}
						Thread.sleep(200 * Math.abs(bulundugu_kat - hedef_kat));
						bulundugu_kat = hedef_kat;

						while (Degiskenler.kuyruk_cikan[bulundugu_kat] > 0 && doluluk < 10) {
							doluluk++;
							musterivekat[0]++;
							Degiskenler.kuyrukCikanAzalt(bulundugu_kat, 1);
							Degiskenler.toplamKuyrukAzalt(1);

						}

					}
				}

				if (doluluk == 10) {
					break;
				}
			}

			girisKataYonel();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Iki asansorun ayni anda ayni kata gitmesini engellemek icin hepsinin
	 * hedefleri kontrol ediliyor
	 * 
	 * Eger bu metot true donerse secilen hedefe gidilebilir Eger false donerse o
	 * hedefe gidilemez
	 */
	static Object lock3 = new Object();

	public Boolean digerAsansorHedefleriKarsilastir(int kat) {
		synchronized (lock3) {

			if (Degiskenler.asansor1.getName() != this.getIsim() && Degiskenler.asansor1.isAktif_mi()) {
				if (Degiskenler.asansor1.getHedef_kat() == kat) {
					return false;
				}
			}

			if (Degiskenler.asansor2.getName() != this.getIsim() && Degiskenler.asansor2.isAktif_mi()) {
				if (Degiskenler.asansor2.getHedef_kat() == kat) {
					return false;
				}
			}

			if (Degiskenler.asansor3.getName() != this.getIsim() && Degiskenler.asansor3.isAktif_mi()) {
				if (Degiskenler.asansor3.getHedef_kat() == kat) {
					return false;
				}
			}

			if (Degiskenler.asansor4.getName() != this.getIsim() && Degiskenler.asansor4.isAktif_mi()) {
				if (Degiskenler.asansor4.getHedef_kat() == kat) {
					return false;
				}
			}

			if (Degiskenler.asansor5.getName() != this.getIsim() && Degiskenler.asansor5.isAktif_mi()) {
				if (Degiskenler.asansor5.getHedef_kat() == kat) {
					return false;
				}
			}

			return true;
		}
	}

	public String getYonelim() {
		return yonelim;
	}

	public void setYonelim(String yonelim) {
		this.yonelim = yonelim;
	}

	public int[] getMusterivekat() {
		return musterivekat;
	}

	public void setMusterivekat(int[] musterivekat) {
		this.musterivekat = musterivekat;
	}

	public String getIsim() {
		return isim;
	}

	public void setIsim(String isim) {
		this.isim = isim;
	}

	public boolean isAktif_mi() {
		return aktif_mi;
	}

	public void setAktif_mi(boolean aktif_mi) {
		this.aktif_mi = aktif_mi;
	}

	public boolean isHareket_ediyor_mu() {
		return hareket_ediyor_mu;
	}

	public void setHareket_ediyor_mu(boolean hareket_ediyor_mu) {
		this.hareket_ediyor_mu = hareket_ediyor_mu;
	}

	public int getBulundugu_kat() {
		return bulundugu_kat;
	}

	public void setBulundugu_kat(int bulundugu_kat) {
		this.bulundugu_kat = bulundugu_kat;
	}

	public int getHedef_kat() {
		return hedef_kat;
	}

	public void setHedef_kat(int hedef_kat) {
		this.hedef_kat = hedef_kat;
	}

	public int getDoluluk() {
		return doluluk;
	}

	public void setDoluluk(int doluluk) {
		this.doluluk = doluluk;
	}
}

public class Main {
	public static void main(String args[]) {

		Degiskenler.baslat();

	}
}

class Cikti extends Thread implements Runnable {
	/*
	 * Ekrana threadler ile ilgili bilgilerin yazildigi thread
	 */
	Thread cikti = new Thread();
	Object lock1 = new Object();

	private void asansorBilgileriYazdri(Asansor asansor) {
		System.out.println(asansor.getIsim());
		System.out.println("\t\t Aktiflik: " + asansor.isAktif_mi());
		System.out.println("\t\t Hareketlilik: " + asansor.isHareket_ediyor_mu());
		System.out.println("\t\t Bulundugu Kat: " + asansor.getBulundugu_kat());
		System.out.println("\t\t Hedef Kat: " + asansor.getHedef_kat());
		System.out.println("\t\t Yonelimi: " + asansor.getYonelim());
		System.out.println("\t\t Kapasite: 10");
		System.out.println("\t\t Anlik Doluluk: " + asansor.getDoluluk());
		System.out.print("\t\t Icerisi: [");
		if (asansor.getHedef_kat() == 0) {
			System.out.print("(" + asansor.getMusterivekat()[0] + ",0)");
		} else {
			for (int i = 1; i < 5; i++) {
				System.out.print("(" + asansor.getMusterivekat()[i] + "," + i + ")");
				if (i != 4) {
					System.out.print(" , ");
				}

			}
		}
		System.out.println(" ]");
	}

	@Override
	public void run() {

		try {

			while (true) {
				synchronized (lock1) {

					int giren = 0;
					for (int i = 0; i < Degiskenler.kuyruk_giren.size(); i++) {
						if (i % 2 == 0) {
							giren += Degiskenler.kuyruk_giren.get(i);
						}
					}
					System.out.println("-----------------------------------------------");
					System.out.println("-----------------------------------------------");
					System.out.println("-----------------------------------------------");

					System.out.println();
					System.out.println("---Katlardaki ve Kuyruktaki Musteriler---");
					for (int i = 0; i < 5; i++) {
						if (i != 0) {
							System.out.println(
									i + ". kat " + (Degiskenler.kattaki_musteri[i] + Degiskenler.kuyruk_cikan[i])
											+ "  Kuyruktaki: " + Degiskenler.kuyruk_cikan[i]);
						} else if (i == 0) {
							System.out
									.println(i + ". kat " + Degiskenler.kattaki_musteri[i] + "  Kuyruktaki: " + giren);
						}

					}

					System.out.println();
					System.out.println();
					System.out.println();
					System.out.println("---Asansorler---");
					System.out.println();
					this.asansorBilgileriYazdri(Degiskenler.asansor1);
					this.asansorBilgileriYazdri(Degiskenler.asansor2);
					this.asansorBilgileriYazdri(Degiskenler.asansor3);
					this.asansorBilgileriYazdri(Degiskenler.asansor4);
					this.asansorBilgileriYazdri(Degiskenler.asansor5);

					System.out.println();
					System.out.println();
					System.out.println("---Katlardaki Kuyruklar");

					System.out.print("0.kat:  [");

					for (int i = 0; i < (Degiskenler.kuyruk_giren.size() + 1) / 2; i++) {
						System.out.print("(" + Degiskenler.kuyruk_giren.get(i * 2) + ","
								+ Degiskenler.kuyruk_giren.get(i * 2 + 1) + ")");
						if (i != ((Degiskenler.kuyruk_giren.size() + 1) / 2) - 1) {
							System.out.print(" , ");
						}
					}
					System.out.println("]");

					for (int i = 1; i < 5; i++) {
						System.out.println(i + ". kat: [(" + Degiskenler.kuyruk_cikan[i] + ",0)]");
					}

					System.out.println();

				}

				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

}
