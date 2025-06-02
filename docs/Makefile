# Makefile for Sphinx documentation

# You can set these variables from the command line, and also
# from the environment for the first two.
SPHINXOPTS    ?=
SPHINXBUILD  ?= sphinx-build
SOURCEDIR    = .
BUILDDIR     = _build

# Put it first so that "make" without argument is like "make help".
help:
	@$(SPHINXBUILD) -M help "$(SOURCEDIR)" "$(BUILDDIR)" $(SPHINXOPTS) $(O)

.PHONY: help Makefile

# Custom targets for MediSys documentation

# Convert markdown files and build HTML
html-full: convert html

# Convert markdown files to RST
convert:
	@echo "Converting Markdown files to reStructuredText..."
	python convert_markdown.py
	@echo "Conversion completed!"

# Clean build directory and converted files
clean-all: clean
	@echo "Cleaning converted RST files..."
	rm -f quick-start.rst installation.rst user-manual.rst developer-guide.rst
	rm -f faq.rst contributing.rst readme.rst architecture.rst api-reference.rst
	rm -f admin-guide.rst security.rst deployment.rst troubleshooting.rst
	rm -f changelog.rst license.rst contact.rst
	@echo "All clean!"

# Build and serve documentation locally
serve: html
	@echo "Starting local documentation server..."
	python -m http.server 8000 --directory _build/html
	@echo "Documentation available at http://localhost:8000"

# Build documentation with verbose output
verbose:
	@$(SPHINXBUILD) -v -b html "$(SOURCEDIR)" "$(BUILDDIR)/html" $(SPHINXOPTS) $(O)

# Build documentation and check for warnings
strict:
	@$(SPHINXBUILD) -W -b html "$(SOURCEDIR)" "$(BUILDDIR)/html" $(SPHINXOPTS) $(O)

# Build documentation for production (with all formats)
production: convert
	@echo "Building production documentation..."
	@$(SPHINXBUILD) -b html "$(SOURCEDIR)" "$(BUILDDIR)/html" $(SPHINXOPTS) $(O)
	@$(SPHINXBUILD) -b epub "$(SOURCEDIR)" "$(BUILDDIR)/epub" $(SPHINXOPTS) $(O)
	@$(SPHINXBUILD) -b latex "$(SOURCEDIR)" "$(BUILDDIR)/latex" $(SPHINXOPTS) $(O)
	@echo "Production build completed!"

# Check documentation for issues
check: convert
	@echo "Checking documentation for issues..."
	@$(SPHINXBUILD) -b linkcheck "$(SOURCEDIR)" "$(BUILDDIR)/linkcheck" $(SPHINXOPTS) $(O)
	@$(SPHINXBUILD) -b spelling "$(SOURCEDIR)" "$(BUILDDIR)/spelling" $(SPHINXOPTS) $(O)
	@echo "Documentation check completed!"

# Build documentation and open in browser
open: html
	@echo "Opening documentation in browser..."
	@python -c "import webbrowser; webbrowser.open('file://$(PWD)/_build/html/index.html')"

# Install documentation dependencies
install-deps:
	@echo "Installing documentation dependencies..."
	pip install -r requirements.txt
	@echo "Dependencies installed!"

# Update documentation dependencies
update-deps:
	@echo "Updating documentation dependencies..."
	pip install --upgrade -r requirements.txt
	@echo "Dependencies updated!"

# Catch-all target: route all unknown targets to Sphinx using the new
# "make mode" option.  $(O) is meant as a shortcut for $(SPHINXOPTS).
%: Makefile
	@$(SPHINXBUILD) -M $@ "$(SOURCEDIR)" "$(BUILDDIR)" $(SPHINXOPTS) $(O)
