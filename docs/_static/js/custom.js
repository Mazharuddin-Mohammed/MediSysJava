// Custom JavaScript for MediSys documentation

document.addEventListener('DOMContentLoaded', function() {
    // Add copy button functionality
    const codeBlocks = document.querySelectorAll('pre');
    codeBlocks.forEach(function(block) {
        const button = document.createElement('button');
        button.className = 'copy-button';
        button.textContent = 'Copy';
        button.addEventListener('click', function() {
            navigator.clipboard.writeText(block.textContent);
            button.textContent = 'Copied!';
            setTimeout(function() {
                button.textContent = 'Copy';
            }, 2000);
        });
        block.appendChild(button);
    });
});
