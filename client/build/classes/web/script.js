// API URL
const apiUrl = 'http://www.52ac.tech/api/exam/getExamScoresByExamId2';

// 模拟获取考试名称的 API (你可以直接替换为真实的API)
async function getExamNameById(examId) {
    // 模拟的API调用
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve({ name: '期中考试' });
        }, 1000);
    });
}

// 模拟获取成绩数据的 API (替换为真实的API)
async function getExamScoresByExamId(data) {
    // 使用 Fetch 来调用 API
    const response = await 
    
    fetch(apiUrl, {
        method: 'POST',
        //mode: 'no-cors',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    });

    return response.json(); // 假设返回的数据为 JSON 格式
}

// 渲染表格数据
function renderTable(data) {
    const tableContainer = document.getElementById('table-container');

    if (!data || data.length === 0) {
        tableContainer.innerHTML = '<p>No data available.</p>';
        return;
    }

    let table = `<table>
        <thead>
            <tr>
                <th>序号</th>
                <th>学号</th>
                <th>班级</th>
                <th>分数</th>
                <th>题解数</th>
                <th>提交数</th>
                <th>已提交</th>
                <th>正在做</th>
                <th>未做</th>
            </tr>
        </thead>
        <tbody>`;

    data.forEach((row, index) => {
        table += `<tr>
            <td>${index + 1}</td>
            <td>${row.studentNo}</td>
            <td>${row.banji}</td>
            <td>${(row.score * 100).toFixed(2)}</td>
            <td>${row.solve}</td>
            <td>${row.submit}</td>
            <td>${renderMultiLine(row.submited)}</td>
            <td>${renderMultiLine(row.doing)}</td>
            <td>${renderMultiLine(row.undo)}</td>
        </tr>`;
    });

    table += `</tbody></table>`;
    tableContainer.innerHTML = table;
}

// 渲染多行文本内容
function renderMultiLine(array) {
    if (!array) return '';
    return array.join('、').replace(/(.{10})/g, '$1\n');
}

// 渲染分页
function renderPagination(total, pageSize, currentPage) {
    const paginationContainer = document.getElementById('pagination');
    const totalPages = Math.ceil(total / pageSize);

    let paginationHTML = '';

    for (let i = 1; i <= totalPages; i++) {
        paginationHTML += `<span class="page-item ${i === currentPage ? 'active-page' : ''}" data-page="${i}">${i}</span> `;
    }

    paginationContainer.innerHTML = paginationHTML;

    // 添加分页点击事件
    document.querySelectorAll('.page-item').forEach(item => {
        item.addEventListener('click', function () {
            const page = parseInt(this.getAttribute('data-page'));
            loadExamScores(page);
        });
    });
}

// 加载考试成绩
async function loadExamScores(pageNum = 1) {
    const examId = '362'; // 这里的 examId 根据实际情况获取
    const pageSize = 100;

    const params = {
        examId: examId,
        fromclient: false,
        pageNum: pageNum,
        pageSize: pageSize
    };

    try {
        const result = await getExamScoresByExamId(params);

        if (result.code === 0) {
            const data = result.data.list;
            renderTable(data);
            renderPagination(result.data.total, pageSize, pageNum);
        } else {
            document.getElementById('notifyMsg').innerText = result.message;
        }
    } catch (error) {
        console.error('Failed to fetch exam scores:', error);
        document.getElementById('notifyMsg').innerText = '获取本场考试成绩失败！';
    }
}

// 获取并显示考试名称
async function loadExamName() {
    const examId = '362'; // 根据实际情况获取 examId
    try {
        const result = await getExamNameById(examId);
        document.getElementById('exam-name').innerText = `${result.name} 考试成绩`;
    } catch (error) {
        console.error('Failed to fetch exam name:', error);
        document.getElementById('notifyMsg').innerText = '获取考试信息失败';
    }
}


// 页面加载时执行
window.onload = function () {
    loadExamName();
    loadExamScores();
};
