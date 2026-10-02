import http from 'k6/http';
import { check } from 'k6';
import { Counter, Rate } from 'k6/metrics';

const successfulRequests = new Counter('login_success_total');
const blockedRequests = new Counter('login_blocked_429_total');
const unexpectedStatusRate = new Rate('login_unexpected_status');

export const options = {
    scenarios: {
        login_spam: {
            executor: 'constant-arrival-rate',

            // Số request mỗi timeUnit
            rate: Number(__ENV.RATE || 50),
            timeUnit: '1s',

            duration: __ENV.DURATION || '1m',

            // VU được khởi tạo sẵn
            preAllocatedVUs: Number(__ENV.PRE_VUS || 20),

            // k6 có thể tăng thêm VU nếu không giữ được target RPS
            maxVUs: Number(__ENV.MAX_VUS || 100),
        },
    },

    thresholds: {
        // Không có status ngoài 2xx và 429
        login_unexpected_status: ['rate<0.01'],

        // k6 phải đủ VU để phát request theo target rate
        dropped_iterations: ['count==0'],

        // Ngưỡng tham khảo, điều chỉnh sau khi có baseline
        http_req_duration: ['p(95)<500'],
    },

    // Không giữ response body trong memory vì test này chỉ cần status
    discardResponseBodies: true,
};

const BASE_URL =
    __ENV.BASE_URL || 'http://localhost:20001';

const LOGIN_PATH =
    __ENV.LOGIN_PATH || '/api/v1/auth/login';

const USERNAME =
    __ENV.USERNAME || 'hai1994';

const PASSWORD =
    __ENV.PASSWORD || '123456789';

const payload = JSON.stringify({
    username: USERNAME,
    password: PASSWORD,
});

const params = {
    headers: {
        'Content-Type': 'application/json',
    },

    tags: {
        name: 'POST login',
    },
};

export default function () {
    const response = http.post(
        `${BASE_URL}${LOGIN_PATH}`,
        payload,
        params
    );

    const isSuccessful =
        response.status >= 200 && response.status < 300;

    const isRateLimited =
        response.status === 429;

    if (isSuccessful) {
        successfulRequests.add(1);
    }

    if (isRateLimited) {
        blockedRequests.add(1);
    }

    unexpectedStatusRate.add(
        !isSuccessful && !isRateLimited
    );

    check(response, {
        'status is 2xx or 429': () =>
            isSuccessful || isRateLimited,
    });
}
