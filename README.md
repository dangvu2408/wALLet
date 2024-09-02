![Static Badge](https://img.shields.io/badge/version-0.0.1-blue)
![Static Badge](https://img.shields.io/badge/release-02%2F09%2F2024-blue)
![Static Badge](https://img.shields.io/badge/build-partial%20success-orange)

# ỨNG DỤNG QUẢN LÍ TÀI CHÍNH wALLet
Hướng dẫn sử dụng bản thử nghiệm (ver 0.0.1 - release 01/09/2024). Ứng dụng giúp người dùng theo dõi các khoản thu - chi, quản lý dòng tiền ra - vào. Ứng dụng xây dựng bằng ngôn ngữ Java trên nền tảng Android.
<img src="https://github.com/dangvu2408/wALLet/blob/master/app/src/main/res/drawable/fullbanner.png"> 

## 1. Cài đặt
Cài đặt dự án trong Terminal của Visual Studio Code: 
```
git clone https://github.com/dangvu2408/wALLet.git
``` 
Trong file `Constans.java` có đường dẫn `app\src\main\java\com\example\walletapp\Constants.java`, chỉnh sửa IP của localhost:
```
public static final String BASE_IP = "192.168.1.139";
```
Tiến hành chạy ứng dụng trên thiết bị Android của bạn, khuyến khích sử dụng Android Studio để build app. 

## 2. Hướng dẫn sử dụng ứng dụng
### 2.1. Đăng kí tạo tài khoản mới

<img src="https://github.com/dangvu2408/wALLet/blob/master/app/src/main/res/drawable/guide001.png"> 

**Bước 1:** Ở màn hình đăng nhập, ấn vào "Tạo tài khoản". \
**Bước 2:** Nhập số điện thoại ***thường xuyên sử dụng***, đây cũng chính là username để bạn đăng nhập. \
**Bước 3:** Nhập mã OTP được gửi về số điện thoại vừa nhập *(tính năng này đang được cập nhật, hãy ấn "Tiếp tục" để chuyển sang màn hình tiếp theo)*. \
**Bước 4:** Nhập mật khẩu đăng nhập.

<img src="https://github.com/dangvu2408/wALLet/blob/master/app/src/main/res/drawable/guide002.png"> 

**Bước 5:** Điền các thông tin cá nhân như họ tên, ngày sinh và giới tính, ở màn hình này người dùng **không được** quay lại màn hình trước (màn hình OTP). \
**Bước 6:** Xác nhận lại thông tin đăng kí và ấn "Xác nhận". \
**Bước 7:** Khi thông tin đăng kí được xác nhận, ấn "Quay lại trang đăng nhập" để đăng nhập bằng tài khoản mới đăng kí. 

### 2.2. Đăng nhập và sử dụng các chức năng của app
Ứng dụng hiện đang trong quá trình hoàn thiện, sẽ có một số chức năng chưa thực hiện được... \
**2.2.1.** Sau khi đăng nhập với username là số điện thoại và password bạn đã đăng kí, nếu đăng nhập thành công, màn hình HomeFragment (Trang chủ) sẽ xuất hiện

<img src="https://github.com/dangvu2408/wALLet/blob/master/app/src/main/res/drawable/guide003.png"> 

***CHÚ Ý:*** Nếu dòng chữ "Xin chào, null" xuất hiện, hãy chuyển sang màn hình "AnalyzeFragment - Phân tích" sau đó quay lại, có thể do dữ liệu chưa cập nhật hết... \
Ở màn hình HomeFragment (Trang chủ) có các chức năng như Thêm giao dịch, Thống kê dòng tiền, Thống kê các giao dịch có giá trị lớn, các giao dịch gần đây, Hiển thị số dư hiện có. \
Ấn vào dòng chữ "Xem tất cả" bên cạnh mục "Giao dịch gần đây", ta sẽ chuyển sang màn hình RecentTransActivity (Giao dịch gần đây). Màn hình này có tổng hợp dòng tiền vào/ra trong tháng hiện tại và danh sách các giao dịch gần đây, các giao dịch được sắp xếp theo thứ tự "giảm dần" về thời gian. Ở mục này, bạn có thể chỉnh sửa, xóa giao dịch hiện có. \
**2.2.2.** Bạn có thể thêm các giao dịch qua mục "Thêm giao dịch"

<img src="https://github.com/dangvu2408/wALLet/blob/master/app/src/main/res/drawable/guide004.png"> 

Mục "Thêm giao dịch" ở màn hình HomeFragment (Trang chủ), có 4 mục để bạn có thể thêm giao dịch, đó là "Khoản chi", "Khoản thu", "Lãi suất" và "Khoản vay", mỗi loại giao dịch có các loại giao dịch con cụ thể cho bạn lựa chọn, đồng thời từng màn hình thêm giao dịch có mục thêm số tiền (*mục này không được trống*), ghi chú cho giao dịch, chọn nhóm cụ thể (*bắt buộc*) và chọn ngày thực hiện giao dịch (*cũng bắt buộc*). Sau đó bạn có thể thêm giao dịch bằng cách ấn nút "LƯU", trong trường hợp Internet của bạn kết nối ổn định. \
**2.2.3.** Và ba màn hình chính còn lại...

<img src="https://github.com/dangvu2408/wALLet/blob/master/app/src/main/res/drawable/guide005.png"> 

Màn hình QueryFragment (Truy vấn) có chức năng truy vấn giao dịch trong khoảng thời gian được chọn cụ thể. Màn hình AnalyzeFragment (Phân tích) có chức năng thống kê dữ liệu và biểu thị lên biểu đồ (gồm biểu đồ cột và biểu đồ hình quạt). Cuối cùng là màn hình ProfileFragment (Tôi) gồm các chức năng liên quan đến bảo mật, đăng nhập, cài đặt thông báo, chỉnh sửa thông tin... \
***CHÚ Ý:*** Sau khi bạn thêm các giao dịch, sửa hoặc xóa giao dịch, dữ liệu sẽ không cập nhật ngay, để cập nhật bạn cần ấn "Cập nhật dữ liệu" ở màn hình ProfileFragment (Tôi), sau đó bạn đợi tầm vài giây thì dữ liệu sẽ được cập nhật, và đừng quên nếu dòng chữ "Xin chào, null" xuất hiện ở màn hình HomeFragment (Trang chủ), hãy chuyển sang màn hình "AnalyzeFragment - Phân tích" sau đó quay lại, có thể do dữ liệu chưa cập nhật hết... 

## 3. Tổng quan về dự án
Ứng dụng được viết bằng ngôn ngữ Java trên nền tảng Android. Ứng dụng yêu cầu thiết bị sử dụng hệ điều hành Android có SDK phiên bản tối thiểu là 27 (Android 8.1 Oreo - Oatmeal Cookie) và tối đa là 34 (Android 14 - Upside Down Cake). Điều này có nghĩa là ứng dụng sẽ hoạt động trên các thiết bị có phiên bản Android từ 8.1 trở lên.
### 3.1. Các thư viện sử dụng trong dự án
**3.1.1. [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart)** 

MPAndroidChart là một thư viện mạnh mẽ và linh hoạt, giúp dễ dàng tích hợp các biểu đồ vào ứng dụng. Thư viện hỗ trợ nhiều loại biểu đồ như biểu đồ đường (Line Chart), biểu đồ cột (Bar Chart), biểu đồ hình quạt (Pie Chart) và nhiều loại biểu đồ khác... 
>Cài đặt thư viện MPAndroidChart: 
```
implementation("com.github.PhilJay:MPAndroidChart:v3.1.0") //build.geadle.kts(:app)
```
```
maven(url = "https://jitpack.io") //settings.gradle.kts
```
**3.1.2. [android-gif-drawable](https://github.com/koral--/android-gif-drawable)** 

android-gif-drawable là một thư viện giúp hiển thị các ảnh GIF động trong ứng dụng một cách dễ dàng và hiệu quả. Thư viện này cung cấp một ImageView tùy chỉnh có khả năng hiển thị GIF mà không ảnh hưởng lớn đến hiệu suất của ứng dụng, ngay cả với các GIF lớn hoặc phức tạp...  
>Cài đặt thư viện android-gif-drawable: 
```
implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.29")
```
**3.1.3. [PinView](https://github.com/ChaosLeung/PinView)** 

PinView là một thư viện tùy chỉnh, giúp tạo ra giao diện nhập mã PIN hoặc mã xác thực một cách đơn giản và tinh tế. Được phát triển bởi ChaosLeung, thư viện này cung cấp một EditText dạng lưới, nơi người dùng có thể nhập các ký tự số hoặc chữ trong từng ô riêng biệt...  
>Cài đặt thư viện PinView: 
```
implementation("io.github.chaosleung:pinview:1.4.4")
```
**3.1.4. [Volley](https://google.github.io/volley/)** 

Volley là một thư viện mạng mạnh mẽ và dễ sử dụng dành cho Android, được phát triển bởi Google. Nó giúp đơn giản hóa việc thực hiện các yêu cầu HTTP và xử lý các phản hồi từ server. Volley đặc biệt hữu ích trong các tình huống yêu cầu mạng phức tạp như tải hình ảnh, gửi form, hoặc quản lý các API RESTful.  
>Cài đặt thư viện Volley: 
```
implementation("com.android.volley:volley:1.2.1")
```
### 3.2. Server và cơ sở dữ liệu
Ứng dụng sử dụng XAMPP, một môi trường máy chủ web tích hợp, để triển khai máy chủ localhost. XAMPP bao gồm các thành phần chính như Apache (máy chủ web), MySQL/MariaDB (hệ quản trị cơ sở dữ liệu), và PHP (ngôn ngữ kịch bản phía server). Điều này cho phép tạo môi trường máy chủ giả lập trên máy tính cá nhân để thực hiện các tác vụ thử nghiệm và phát triển trước khi triển khai trên môi trường thật.

Trong ứng dụng, dữ liệu được lưu trữ và truy xuất từ cơ sở dữ liệu MySQL thông qua các truy vấn SQL thông qua các file PHP, cụ thể các file xem tại [đây](https://github.com/dangvu2408/wALLet_app-BE).

Ứng dụng gửi các yêu cầu (request) HTTP từ thiết bị Android tới server localhost, nơi các tập tin PHP sẽ xử lý yêu cầu và thực hiện các truy vấn dữ liệu trên MySQL. Sau đó, kết quả trả về (response) được sử dụng để hiển thị thông tin hoặc cập nhật dữ liệu trong ứng dụng.

Về mặt dữ liệu, CSDL của ứng dụng bao gầm 2 bảng:

Bảng `userlogin`:
| Tên | Kiểu | Bảng mã đối chiếu | Null | Mặc định | Thêm |
| :---: | :---: | :---: | :---: | :---: | :---: |
| username | varchar(255) | utf8_general_ci | Không | Không | **Primary Key** |
| password | varchar(255) | utf8_general_ci | Không | Không | |
| fullname | varchar(255) | utf8_general_ci | Có | NULL | |
| dob | varchar(255) | utf8_general_ci | Có | NULL | |
| gender | varchar(255) | utf8_general_ci | Có | NULL | |

Bảng `userdatas`:
| Tên | Kiểu | Bảng mã đối chiếu | Null | Mặc định | Thêm |
| :---: | :---: | :---: | :---: | :---: | :---: |
| mainType | varchar(255) | utf8_general_ci | Không | Không |  |
| type | varchar(255) | utf8_general_ci | Không | Không | |
| money | varchar(255) | utf8_general_ci | Không | Không | |
| date | varchar(255) | utf8_general_ci | Không | Không | |
| description | varchar(255) | utf8_general_ci | Không | Không | |
| userid | varchar(255) | utf8_general_ci | Không | Không | **Foreign Key** |
| userfullname | varchar(255) | utf8_general_ci | Không | Không | |
| id_transaction | int(11) |  | Không | Không |  **Primary Key**<br>  **AUTO_INCREMENT** |

Quan hệ giữa hai bảng được thể hiện qua ER Diagram như sau: 

<img src="https://github.com/dangvu2408/wALLet/blob/master/app/src/main/res/drawable/er_diagram.jpg"> 

UPDATING...
