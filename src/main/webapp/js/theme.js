(function () {
    var STORAGE_KEY = 'smart-parking-theme';

    function preferredTheme() {
        return window.matchMedia && window.matchMedia('(prefers-color-scheme: dark)').matches
            ? 'dark'
            : 'light';
    }

    function getTheme() {
        return localStorage.getItem(STORAGE_KEY) || preferredTheme();
    }

    function setTheme(theme) {
        document.documentElement.setAttribute('data-theme', theme);
        localStorage.setItem(STORAGE_KEY, theme);
    }

    function buttonText(theme) {
        return theme === 'dark' ? '☀ Light' : '🌙 Dark';
    }

    function createToggle() {
        var nav = document.querySelector('.navbar');
        if (!nav) return;

        var rightSide = nav.querySelector('div') || nav;
        var btn = document.createElement('button');
        btn.type = 'button';
        btn.className = 'theme-toggle';

        var theme = getTheme();
        btn.textContent = buttonText(theme);
        btn.setAttribute('aria-label', 'Toggle dark and light mode');

        btn.addEventListener('click', function () {
            var current = document.documentElement.getAttribute('data-theme') || 'light';
            var next = current === 'dark' ? 'light' : 'dark';
            setTheme(next);
            btn.textContent = buttonText(next);
        });

        rightSide.appendChild(btn);
    }

    setTheme(getTheme());
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', createToggle);
    } else {
        createToggle();
    }
})();
