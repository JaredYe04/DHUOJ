// 请求的 URL
const url = 'http://www.52ac.tech/api/exam/getExamScoresByExamId2';

// 请求的参数
const data = {
    examId: '360',
    fromclient: false,
    pageNum: 1,
    pageSize: 100
};

// 发送 POST 请求的函数
async function getExamScores() {
    try {
        // 使用 fetch 发送 POST 请求
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'  // 设置请求头为 JSON 格式
            },
            body: JSON.stringify(data)  // 将请求体转为 JSON 字符串
        });
        //console.log(data)
        // 获取响应的 JSON 数据
        const result = await response.json();
    
        // 控制台输出响应内容
        //console.log(result);
        //console.log(result.data.list);
        console.log(JSON.stringify(result));
        let list=result.data.list;
        list.forEach(element => {
            console.log(JSON.stringify(element));
        });
    } catch (error) {
        // 如果请求失败，输出错误信息
        console.error('Error:', error);
    }
}

// 调用函数发送请求
getExamScores();
