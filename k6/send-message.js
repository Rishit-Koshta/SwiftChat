import http from 'k6/http';
import { check, sleep } from 'k6';

export const options = {
    vus: 200,
    duration: '1m',

    thresholds: {
        http_req_failed: ['rate<0.01'],
        http_req_duration: ['p(95)<500'],
    },
};

const BASE_URL = 'http://localhost:8081';

const CHAT_ID = 'cdadfad1-8f86-42e7-8f8c-397e86745004';
const USER_ID = '0cdcb2e4-0c31-4ad7-ad75-c7d67fecbc1f';

export default function () {

    const payload = JSON.stringify({
        chatId: CHAT_ID,
        senderID: USER_ID,
        content: `Performance Test ${__VU}-${__ITER}`
    });

    const params = {
        headers: {
            'Content-Type': 'application/json',
        },
    };

    const res = http.post(
        `${BASE_URL}/message/sendMessage`,
        payload,
        params
    );

    check(res, {
        'status is 201': (r) => r.status === 201,
    });


}