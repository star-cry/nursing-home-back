package neo.careplus.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private int status;

    private boolean success;

    private String message;

    private T data;

    public static <T> ApiResponse<T> success( T data) {
        return new ApiResponse<>(HttpStatus.OK.value(), true, "요청이 성공적으로 처리되었습니다.", data);
    }

    public static <T> ApiResponse<T> fail(HttpStatus status, String message) {
        return new ApiResponse<>(status.value(), false, message, null);
    }
}
