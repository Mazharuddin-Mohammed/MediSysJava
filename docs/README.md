# MediSys Documentation

This directory contains the Sphinx documentation for MediSys Hospital Management System.

## Building Documentation

### Prerequisites

Install the required dependencies:

```bash
pip install -r requirements.txt
```

### Local Development

1. **Convert Markdown to RST** (if needed):
   ```bash
   python convert_markdown.py
   ```

2. **Build HTML documentation**:
   ```bash
   make html
   ```

3. **Serve documentation locally**:
   ```bash
   make serve
   ```
   
   Then open http://localhost:8000 in your browser.

### Available Make Targets

- `make html` - Build HTML documentation
- `make html-full` - Convert markdown and build HTML
- `make convert` - Convert markdown files to RST
- `make clean` - Clean build directory
- `make clean-all` - Clean build and converted files
- `make serve` - Build and serve documentation locally
- `make production` - Build all formats (HTML, PDF, EPUB)
- `make check` - Check documentation for issues
- `make open` - Build and open in browser

### Read the Docs

This documentation is automatically built and deployed to Read the Docs when changes are pushed to the repository.

**Documentation URL**: https://medisysjava.readthedocs.io/

### File Structure

```
docs/
├── conf.py                 # Sphinx configuration
├── index.rst              # Main documentation index
├── requirements.txt       # Python dependencies
├── convert_markdown.py    # Markdown to RST converter
├── Makefile               # Build automation
├── _static/               # Static files (CSS, JS, images)
├── _templates/            # Custom templates
└── wiki/                  # Original markdown files
```

### Configuration

The documentation is configured in `conf.py` with:

- **Theme**: sphinx_rtd_theme (Read the Docs theme)
- **Extensions**: MyST parser, copy button, design elements
- **Formats**: HTML, PDF, EPUB
- **Features**: Search, navigation, responsive design

### Contributing

When adding new documentation:

1. **Markdown files**: Add to `docs/wiki/` directory
2. **RST files**: Add directly to `docs/` directory
3. **Update index**: Add new files to `index.rst` toctree
4. **Test locally**: Run `make html` to verify

### Troubleshooting

**Build errors**:
- Check `requirements.txt` dependencies are installed
- Verify RST syntax in converted files
- Check Sphinx configuration in `conf.py`

**Missing images**:
- Ensure images are in `_static/images/` directory
- Check image paths in RST files
- Verify image formats are supported

**Conversion issues**:
- Check markdown syntax in source files
- Review `convert_markdown.py` script
- Manually fix RST output if needed

For more help, see the main [Contributing Guide](../CONTRIBUTING.md).
