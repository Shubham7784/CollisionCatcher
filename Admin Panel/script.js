(function attachErrorOverlay(){
  const overlay = document.createElement('div');
  overlay.id = 'debug-overlay';
  overlay.style.cssText = 'position:fixed;right:12px;bottom:12px;z-index:99999;background:#2b1010;color:#ffdede;padding:10px;border-radius:8px;max-width:420px;box-shadow:0 6px 18px rgba(0,0,0,0.6);font-family:monospace;font-size:13px;display:none;white-space:pre-wrap';
  document.body.appendChild(overlay);

  function show(msg){
    overlay.style.display = 'block';
    overlay.textContent = msg;
    console.error('DEBUG OVERLAY:', msg);
  }

  window.showDebugOverlay = show;

  window.addEventListener('error', function(ev){
    try {
      show('Error: ' + (ev.message || 'unknown') + '\nAt: ' + (ev.filename || '') + ':' + (ev.lineno || '') + ':' + (ev.colno || ''));
    } catch(e) { console.error('overlay error', e); }
  });

  window.addEventListener('unhandledrejection', function(ev){
    try {
      const text = (ev.reason && ev.reason.stack) ? ev.reason.stack : String(ev.reason);
      show('Unhandled Promise rejection:\n' + text);
    } catch(e) { console.error('overlay error', e); }
  });

  overlay.addEventListener('click', ()=> overlay.style.display = 'none');
})();

const $ = (selector) => document.querySelector(selector);
const $$ = (selector) => Array.from(document.querySelectorAll(selector));

const showElement = (el) => { if (!el) return; el.style.display = ''; };
const hideElement = (el) => { if (!el) return; el.style.display = 'none'; };

const setText = (el, text) => { if (!el) return; el.textContent = text; };

/* ------------------------
   UI Elements
   ------------------------ */
const loginBtn = $('#loginBtn');
const noticeEl = $('#notice');
const loginView = $('#loginView');
const adminApp = $('#adminApp');
const signedInUserEl = $('#signedInUser');

const navLinks = $$('.nav a');
const headerName = $('#header-page-name');

const statUsers = $('#stat-total-users');
const statVehicles = $('#stat-total-vehicles');
const statAlerts = $('#stat-active-alerts');

const dashboardAlertsTable = $('#dashboard-alerts-table');
const usersBlock = $('#page-users-block');
const reportsBlock = $('#page-reports-block');
const dashboardGrid = $('#dashboard-grid');

const usersTableBody = $('#users-table-body');
const accidentReportsTable = $('#accident-reports-table');
const theftReportsTable = $('#theft-reports-table');

const userModal = $('#user-modal');
const modalCloseBtn = $('#modal-close-btn');
const modalRemoveUser = $('#modal-remove-user');

const API = {
  baseUrl: 'http://localhost:8080', // <<-- change this to your Spring Boot base
  token: null, // will hold JWT or similar after login
  setToken(t) { this.token = t; localStorage.setItem('admin_token', t); },
  loadToken() { const t = localStorage.getItem('admin_token'); if (t) this.token = t; },
  clearToken() { this.token = null; localStorage.removeItem('admin_token'); }
};

/* ------------------------
   Helper: show/hide notices
   ------------------------ */
function showNotice(message, color = '#ffb4b4') {
  noticeEl.style.display = 'block';
  noticeEl.style.color = color;
  noticeEl.textContent = message;
}

function hideNotice() {
  noticeEl.style.display = 'none';
  noticeEl.textContent = '';
}

/* ------------------------
   Time formatting helper
   ------------------------ */
function formatSimpleTime(date) {
  try {
    if (!date) return '-';
    const d = (date instanceof Date) ? date : new Date(date);
    return d.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
  } catch (e) { return String(date); }
}

/* ------------------------
   API functions (async)
   Expectation: backend returns JSON with appropriate fields.
   JWT Authorization via: Authorization: Bearer <token>
   ------------------------ */
async function apiFetch(path, options = {}) {
  const url = `${API.baseUrl}${path}`;
  const headers = options.headers || {};
  headers['Content-Type'] = 'application/json';
  if (API.token) headers['Authorization'] = 'Bearer ' + API.token;

  try {
    const res = await fetch(url, { ...options, headers });
    const contentType = res.headers.get('content-type') || '';
    let payload = null;
    if (contentType.includes('application/json')) payload = await res.json();
    else payload = await res.text();

    if (!res.ok) {
      const errMsg = (payload && payload.message) ? payload.message : (payload || res.statusText || 'API error');
      throw new Error(errMsg);
    }
    return payload;
  } catch (err) {
    console.error(`API error ${url}:\n${err.stack || err}`);
    throw err;
  }
}

async function apiLogin(username, password) {
  // POST /auth/login -> { token: '...' , user: { username, name } }
  try {
    const body = JSON.stringify({"userName":username,
        "password":password
    });
    const data = await apiFetch('/public/login-admin', { method: 'POST', body });
    if (data) {
      API.setToken(data);
      return data;
    }
    throw new Error('Login response did not include token');
  } catch (err) {
    throw err;
  }
}

async function apiFetchUsers() {
  // GET /users -> [ ... ]
  try {
    return await apiFetch('/admin/getUsers', { method: 'GET' });
  } catch (err) {
    // fallback to mock
    return; 
  }
}

async function apiFetchAlerts() {
  // GET /alerts -> [ ... ]
  try {
    return await apiFetch('/alerts', { method: 'GET' });
  } catch (err) {
    console.warn('Failed to load alerts from backend, using mockAlerts', err);
    return;
  }
}

async function apiGetUser(userId) {
  try {
    return await apiFetch(`/admin/getUsers/${userId}`, { method: 'GET' });
  } catch (err) {
    console.warn('Failed to fetch user from backend, using local find', err);
    return null;
  }
}

async function apiRemoveUser(userId) {
  try{
    return await apiFetch(`/admin/${userId}`,{method : 'DELETE'})
  }
  catch(err)
  {
    console.warn("Failed to delete user !!",err);
    return null;
  }
}
async function apiResolveAlert(alertId) {
  // POST or PATCH /alerts/{id}/resolve -> { success: true }
  try {
    return await apiFetch(`/alerts/${encodeURIComponent(alertId)}/resolve`, { method: 'POST' });
  } catch (err) {
    // fallback: mutate local mock
    console.warn('Resolve alert failed on backend; mutating local mock', err);
    return;
  }
}
async function apiSearch(q) {
  // GET /search?q=...
  try {
    const encoded = encodeURIComponent(q);
    return await apiFetch(`/search?q=${encoded}`, { method: 'GET' });
  } catch (err) {
    console.warn('Search endpoint failed; performing local search', err);
    return;
  }
}

/* ------------------------
   UI rendering functions (use API)
   ------------------------ */
async function renderDashboard() {
  try {
    const alerts = await apiFetchAlerts();
    const activeAlerts = (Array.isArray(alerts) ? alerts : []).filter(a => a.status === 'active');

    const users = await apiFetchUsers() || [];
    setText(statUsers, String(users.length));
    setText(statVehicles, String(users.length));
    setText(statAlerts, String(activeAlerts.length));

    dashboardAlertsTable.innerHTML = '';
    if (activeAlerts.length === 0) {
      dashboardAlertsTable.innerHTML = '<tr><td colspan="4" class="muted" style="padding:14px;text-align:center">No active alerts.</td></tr>';
      return;
    }

    activeAlerts.forEach(alert => {
      const tr = document.createElement('tr');
      tr.className = 'row-hover';
      const typeLabel = alert.type === 'accident'
        ? `<span style="padding:6px 10px;border-radius:999px;font-weight:700;font-size:12px;background:#2b0610;color:#ffb4b4">Accident</span>`
        : `<span style="padding:6px 10px;border-radius:999px;font-weight:700;font-size:12px;background:#2b2a06;color:#fff0b4">Theft</span>`;

      tr.innerHTML = `
        <td class="p-3" style="font-weight:700">${alert.userName}</td>
        <td class="p-3">${alert.typeLabel}</td>
        <td class="p-3">${(alert.location) || 'Unknown address'}</td>
        <td class="p-3">${formatSimpleTime(alert.dateTime)}</td>
      `;
      dashboardAlertsTable.appendChild(tr);
    });
  } catch (err) {
    dashboardAlertsTable.innerHTML = '<tr><td colspan="4" class="muted" style="padding:14px;text-align:center">Failed to load alerts.</td></tr>';
    console.error('renderDashboard failed:\n' + (err.stack || err));
  }
}

async function renderUsersPage() {
  try {
    const users = await apiFetchUsers() || [];
    console.log(users.length);
    usersTableBody.innerHTML = '';
    if (!users || users.length === 0) {
      usersTableBody.innerHTML = '<tr><td colspan="5" class="muted" style="padding:12px;text-align:center">No users registered.</td></tr>';
      return;
    }

    users.forEach(user => {
      const tr = document.createElement('tr');
      tr.className = 'row-hover';
      tr.innerHTML = `
        <td class="p-3" style="font-weight:700">${user.name}</td>
        <td class="p-3">${user.userName}</td>
        <td class="p-3">${user.phoneNo || 'N/A'}</td>
        <td class="p-3"><button class="ghost view-user-btn" data-user-id="${user.userName}" type="button">View Details</button></td>
      `;
      usersTableBody.appendChild(tr);
    });

    $$('.view-user-btn').forEach(btn => {
      btn.addEventListener('click', (ev) => {
        const id = ev.currentTarget.dataset.userId;
        openUserModal(id);
      });
    });
  } catch (err) {
    usersTableBody.innerHTML = '<tr><td colspan="5" class="muted" style="padding:12px;text-align:center">Failed to load users.</td></tr>';
    console.error('renderUsersPage failed:\n' + (err.stack || err));
  }
}

async function renderReportsPage() {
  try {
    const alerts = await apiFetchAlerts();
    const activeAlerts = (Array.isArray(alerts) ? alerts : []).filter(a => a.status === 'active');
    const accidents = activeAlerts.filter(a => a.type === 'accident');
    const thefts = activeAlerts.filter(a => a.type === 'theft');
    const users = await apiFetchUsers();

    accidentReportsTable.innerHTML = '';
    theftReportsTable.innerHTML = '';

    if (accidents.length === 0) {
      accidentReportsTable.innerHTML = '<tr><td colspan="5" class="muted" style="padding:12px;text-align:center">No active accident reports.</td></tr>';
    } else {
      accidents.forEach(alert => {
        const user = users && users.find(u => u.id === alert.userId) || {};
        const row = document.createElement('tr');
        row.className = 'row-hover';
        row.innerHTML = `
          <td class="p-3" style="font-weight:700">${user.name || 'Unknown'}</td>
          <td class="p-3">${(alert.location && alert.location.address) || 'Unknown'}</td>
          <td class="p-3">${formatSimpleTime(alert.time)}</td>
          <td class="p-3"><button class="btn resolve-btn" data-alert-id="${alert.id}" type="button">Resolve</button></td>
        `;
        accidentReportsTable.appendChild(row);
      });
    }

    if (thefts.length === 0) {
      theftReportsTable.innerHTML = '<tr><td colspan="5" class="muted" style="padding:12px;text-align:center">No active theft reports.</td></tr>';
    } else {
      thefts.forEach(alert => {
        const user = users && users.find(u => u.id === alert.userId) || {};
        const row = document.createElement('tr');
        row.className = 'row-hover';
        row.innerHTML = `
          <td class="p-3" style="font-weight:700">${user.name || 'Unknown'}</td>
          <td class="p-3">${user.vehicle ? user.vehicle.plate : 'N/A'}</td>
          <td class="p-3">${(alert.location && alert.location.address) || 'Unknown'}</td>
          <td class="p-3">${formatSimpleTime(alert.time)}</td>
          <td class="p-3"><button class="btn resolve-btn" data-alert-id="${alert.id}" type="button">Resolve</button></td>
        `;
        theftReportsTable.appendChild(row);
      });
    }

    $$('.resolve-btn').forEach(btn => {
      btn.addEventListener('click', async (ev) => {
        const id = parseInt(ev.currentTarget.dataset.alertId, 10);
        try {
          await apiResolveAlert(id);
          await renderDashboard();
          await renderReportsPage();
        } catch (err) {
          showNotice('Failed to resolve alert.');
          console.error('resolve-btn handler error:\n' + (err.stack || err));
        }
      });
    });

  } catch (err) {
    accidentReportsTable.innerHTML = '<tr><td colspan="5" class="muted" style="padding:12px;text-align:center">Failed to load reports.</td></tr>';
    theftReportsTable.innerHTML = '<tr><td colspan="5" class="muted" style="padding:12px;text-align:center">Failed to load reports.</td></tr>';
    console.error('renderReportsPage failed:\n' + (err.stack || err));
  }
}

/* ------------------------
   Modal: open/close + fill from backend
   ------------------------ */
async function openUserModal(userId) {
  try {
    const user = await apiGetUser(userId);
    if (!user) {
      showNotice('User not found.');
      return;
    }

    setText($('#modal-user-name'), user.name || 'N/A');
    setText($('#modal-user-email'), user.userName || 'N/A');
    setText($('#modal-user-phone'), user.phoneNo || 'N/A');
    const contactsList = $('#modal-user-contacts');
    contactsList.innerHTML = '';
    if (!user.members || user.members.length === 0) {
      contactsList.innerHTML = '<li class="muted">No emergency contacts added.</li>';
    } else {
      user.members.forEach(c => {
        const li = document.createElement('li');
        li.style.padding = '8px';
        li.style.marginBottom = '6px';
        li.style.borderRadius = '8px';
        li.style.background = 'linear-gradient(180deg, rgba(255,255,255,0.01), transparent)';
        li.innerHTML = `<strong style="font-weight:700">${c.name}</strong> <span style="float:right">${c.phoneNo}</span>`;
        contactsList.appendChild(li);
      });
    }

    userModal.style.display = 'flex';
    function onBackdropClick(e) {
      if (e.target === userModal) {
        closeModal();
        userModal.removeEventListener('click', onBackdropClick);
      }
    }
    userModal.addEventListener('click', onBackdropClick);
    modalRemoveUser.setAttribute('data-user-id',userId)
  } catch (err) {
    if (window.showDebugOverlay) window.showDebugOverlay('openUserModal failed:\n' + (err.stack || err));
    showNotice('Failed to open user details.');
  }
}

function closeModal() {
  userModal.style.display = 'none';
}

modalCloseBtn.addEventListener('click', closeModal);

async function removeUser(userId)
{
  try{
    const res = await apiRemoveUser(userId);
    if(!res.ok)
    {
      alert("User Removed Successfully!");
      closeModal();
    }
    else
    {
      alert("Cannot Find User!");
    }
    return null;
  }
  catch(err)
  {
    if (window.showDebugOverlay) window.showDebugOverlay('openUserModal failed:\n' + (err.stack || err));
    showNotice('Failed to remove user.');
  }
}

modalRemoveUser.addEventListener('click',(ev)=>{
  const id = ev.currentTarget.dataset.userId;
  removeUser(id);
})

/* ------------------------
   Initialization & login handling
   ------------------------ */
function switchToAdminView(username) {
  try {
    loginView.style.display = 'none';
    adminApp.classList.add('visible');
    adminApp.setAttribute('aria-hidden', 'false');
    setText(signedInUserEl, username || 'Admin');

    adminApp.scrollTop = 0;
    window.scrollTo(0, 0);

    initializeAdminPanel();
  } catch (err) {
    if (window.showDebugOverlay) {
      window.showDebugOverlay('switchToAdminView failed:\n' + (err.stack || String(err)));
    } else {
      console.error('switchToAdminView failed:', err);
      alert('Failed to open admin panel: ' + (err.message || err));
    }
  }
}

API.loadToken(); // preload token if present

loginBtn.addEventListener('click', async () => {
  hideNotice();
  const username = $('#username').value.trim();
  const password = $('#password').value;

  if (!username || !password) {
    showNotice('Please enter credentials.');
    return;
  }

  // Try backend login first
  try {
    const data = await apiLogin(username, password);
    hideNotice();
    switchToAdminView(data.user ? (data.user.name || username) : username);
  } catch (err) {
    console.log('login failed:\n' + (err.stack || err))
    return;
  }
});

/* ------------------------
   Logout
   ------------------------ */
$('#logoutBtn').addEventListener('click', () => {
  hideElement(adminApp);
  adminApp.classList.remove('visible');
  adminApp.setAttribute('aria-hidden', 'true');
  showElement(loginView);

  $('#username').value = '';
  $('#password').value = '';
  hideNotice();

  navLinks.forEach(n => n.classList.remove('active'));
  navLinks.forEach(n => n.classList.add('inactive'));
  $('#nav-dashboard').classList.remove('inactive');
  $('#nav-dashboard').classList.add('active');

  headerName.textContent = 'Dashboard';
  window.scrollTo(0, 0);
  API.clearToken();
});

/* ------------------------
   Navigation wiring
   ------------------------ */
navLinks.forEach(link => {
  link.addEventListener('click', (e) => {
    e.preventDefault();
    const target = link.dataset.target || 'dashboard';

    navLinks.forEach(n => n.classList.remove('active'));
    navLinks.forEach(n => n.classList.add('inactive'));
    link.classList.remove('inactive');
    link.classList.add('active');

    headerName.textContent = link.textContent.trim();

    if (target === 'dashboard') {
      hideElement(usersBlock);
      hideElement(reportsBlock);
      showElement(dashboardGrid);
      renderDashboard();
    } else if (target === 'users') {
      hideElement(dashboardGrid);
      hideElement(reportsBlock);
      showElement(usersBlock);
      renderUsersPage();
    } else if (target === 'reports') {
      hideElement(dashboardGrid);
      hideElement(usersBlock);
      showElement(reportsBlock);
      renderReportsPage();
    }
  });
});

/* ------------------------
   Admin panel initialization
   ------------------------ */
function initializeAdminPanel() {
  renderDashboard();
  renderUsersPage();
  renderReportsPage();

  // $('#globalSearch').addEventListener('keydown', async (e) => {
  //   if (e.key !== 'Enter') return;
  //   const q = e.target.value.trim().toLowerCase();
  //   if (!q) {
  //     renderDashboard();
  //     return;
  //   }

  //   try {
  //     const results = await apiSearch(q);
  //     if (results && results.length) {
  //       $('#nav-users').click();
  //       usersTableBody.innerHTML = '';
  //       results.forEach(user => {
  //         const tr = document.createElement('tr');
  //         tr.className = 'row-hover';
  //         tr.innerHTML = `
  //           <td class="p-3" style="font-weight:700">${user.name}</td>
  //           <td class="p-3">${user.userName}</td>
  //           <td class="p-3">${user.phoneNo || 'N/A'}</td>
  //           <td class="p-3"><button class="ghost view-user-btn" data-user-id="${user.userName}" type="button">View Details</button></td>
  //         `;
  //         usersTableBody.appendChild(tr);
  //       });

  //       $$('.view-user-btn').forEach(btn => {
  //         btn.addEventListener('click', (ev) => openUserModal(ev.currentTarget.dataset.userId));
  //       });
  //     } else {
  //       alert('No results found.');
  //     }
  //   } catch (err) {
  //     alert('Search failed.');
  //     console.error('globalSearch error:\n' + (err.stack || err));
  //   }
  // });

  $('#userSearch').addEventListener('input', async (e) => {
    const q = e.target.value.trim().toLowerCase();
    usersTableBody.innerHTML = '';

    try {
      if (!q) {
        await renderUsersPage();
        return;
      }
      const results = await apiSearch(q);
      (results || []).forEach(user => {
        const tr = document.createElement('tr');
        tr.className = 'row-hover';
        tr.innerHTML = `
          <td class="p-3" style="font-weight:700">${user.name}</td>
          <td class="p-3">${user.userName}</td>
          <td class="p-3">${user.phoneNo || 'N/A'}</td>
          <td class="p-3"><button class="ghost view-user-btn" data-user-id="${user.userName}" type="button">View Details</button></td>
        `;
        usersTableBody.appendChild(tr);
      });

      $$('.view-user-btn').forEach(btn => {
        btn.addEventListener('click', (ev) => openUserModal(ev.currentTarget.dataset.userId));
      });
    } catch (err) {
      console.error('userSearch input handler error:\n' + (err.stack || err));
    }
  });
  $('#notifBtn').addEventListener('click', () => alert('Notifications (demo)'));
}

/* Allow pressing enter on password to login */
$('#password').addEventListener('keydown', (e) => {
  if (e.key === 'Enter') loginBtn.click();
});

/* If token exists, pre-open admin view (optional) */
if (API.token) {
  // attempt to fetch profile or just open UI
  (async () => {
    try {
      // optional: hit /auth/me to get user profile
      const me = await apiFetch('/admin/check-admin-login', { method: 'GET' });
      switchToAdminView(me?me : 'Admin');
    } catch (err) {
      // token invalid, clear
      API.clearToken();
    }
  })();
}
