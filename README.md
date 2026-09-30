<div align="center">

<img src="https://capsule-render.vercel.app/api?type=rect&color=0:0b1220,100:0e7490&height=110&section=header&text=SetHome&fontSize=42&fontColor=22d3ee&fontAlignY=54&desc=Homes%20with%20warmup%20and%20countdown&descSize=13&descColor=94a3b8&descAlignY=80" width="100%" alt="SetHome" />

<p>
<img src="https://img.shields.io/github/v/release/chizzar-dev/Sethome?style=flat&label=release&color=06b6d4&labelColor=0b1220" alt="release" />
<img src="https://img.shields.io/badge/Minecraft-1.8%20%E2%80%93%201.21.11-0891b2?style=flat&labelColor=0b1220" alt="Minecraft 1.8 - 1.21.11" />
<img src="https://img.shields.io/badge/Java-8%2B-155e75?style=flat&labelColor=0b1220&logo=openjdk&logoColor=22d3ee" alt="Java 8+" />
<a href="LICENSE"><img src="https://img.shields.io/github/license/chizzar-dev/Sethome?style=flat&label=license&color=0e7490&labelColor=0b1220" alt="license" /></a>
</p>

</div>

SetHome lets players save named homes and teleport back, with a configurable limit, warmup delay and an on-screen countdown.

*SetHome, oyunculara isimli ev kaydedip geri donme imkani verir; ayarlanabilir limit, bekleme suresi ve ekranda geri sayim icerir.*

## Features · Özellikler
- İsimli evler (`/sethome ev2`) ve ayarlanabilir **ev limiti**
- `/home` ışınlanmasında bekleme süresi + ekranda **geri sayım**
- Hareket edince iptal (configden açılıp kapatılabilir)
- Geri sayım action bar (küçük) veya title olarak gösterilir
- `sethome.bypass` anında ışınlanma, `sethome.unlimited` sınırsız ev

## Installation · Kurulum
1. [Releases](https://github.com/chizzar-dev/Sethome/releases/latest) sayfasından `SetHome.jar` dosyasını indir ve sunucunun `plugins/` klasörüne at.
2. Sunucuyu yeniden başlat.
3. `plugins/SetHome/config.yml` dosyasından limit, süre ve mesajları düzenle.

## Commands · Komutlar
| Komut | Açıklama | Yetki |
|-------|----------|-------|
| `/sethome [isim]` | Bulunduğun yeri ev olarak kaydeder | `sethome.use` |
| `/home [isim]` | Kayıtlı evine ışınlanır | `sethome.use` |
| `/delhome [isim]` | Bir evini siler | `sethome.use` |
| `/homes` | Evlerini listeler | `sethome.use` |

**Alias:** `/ev`, `/evkur`, `/evsil`, `/evler`. İsim verilmezse varsayılan ev (`home`) kullanılır.

## Permissions · Yetkiler
| Yetki | Açıklama | Varsayılan |
|-------|----------|------------|
| `sethome.use` | Ev komutlarını kullanır | herkes |
| `sethome.unlimited` | Sınırsız ev kaydeder | kapalı (verilmeli) |
| `sethome.bypass` | Beklemeden anında ışınlanır | kapalı (verilmeli) |

> `sethome.unlimited` ve `sethome.bypass` varsayılan **kapalıdır** — OP dahil herkes ev limitine
> ve bekleme süresine tabidir. Muaf tutmak istediğin yetkililere bu izinleri açıkça ver
> (örn. LuckPerms ile).

## Configuration · Ayarlar
| Anahtar | Açıklama |
|---------|----------|
| `max-homes` | Oyuncu başına ev limiti |
| `default-home` | İsim verilmeyince kullanılacak ev adı |
| `warmup` | Işınlanma bekleme süresi (saniye, 0 = anında) |
| `cancel-on-move` | Hareket edince iptal et |
| `countdown.type` | `ACTIONBAR` (küçük) veya `TITLE` (ekran ortası) |
| `countdown.message` / `title` / `subtitle` | Geri sayım metinleri (`%time%`) |
| `messages.*` | Tüm mesajlar |

Evler `plugins/SetHome/homes.yml` içinde oyuncu UUID'sine göre saklanır.

## Building · Derleme
```bash
mvn clean package
```
Çıktı · Output: `target/SetHome.jar`

Her push [GitHub Actions](https://github.com/chizzar-dev/Sethome/actions/workflows/build.yml) ile derlenir; `v*` etiketli sürümler jar'la birlikte [Releases](https://github.com/chizzar-dev/Sethome/releases) sayfasına eklenir.
<br><sub>Every push is built by GitHub Actions; tagged `v*` releases attach the jar.</sub>

## License · Lisans
[MIT](LICENSE) — istediğin gibi kullan, değiştir, dağıt · use, modify and distribute freely

<div align="center"><sub>chizzar-dev · Minecraft plugins for 1.8 – 1.21.11 · <a href="https://discord.gg/forges">Discord</a></sub></div>
