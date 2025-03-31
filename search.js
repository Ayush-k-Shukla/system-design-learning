const fs = require('fs');
const path = require('path');

const DIRECTORY_PATH = '.'; // Root directory (GitHub Pages serves from the repo root)
const OUTPUT_FILE = './search-index.json';

// Function to get all markdown files recusively
function getMarkdownFiles(dir) {
  let results = [];
  const files = fs.readdirSync(dir);
  for (const file of files) {
    const filePath = path.join(dir, file);
    const stat = fs.statSync(filePath);
    if (stat.isDirectory()) {
      results = results.concat(getMarkdownFiles(filePath));
    } else if (file.endsWith('.md')) {
      results.push(filePath);
    }
  }
  console.log(results);
  return results;
}

// Create search index
let searchIndex = [];
getMarkdownFiles(DIRECTORY_PATH).forEach((file) => {
  const content = fs.readFileSync(file, 'utf-8');
  searchIndex.push({ file, content });
});

// Save to JSON
fs.writeFileSync(OUTPUT_FILE, JSON.stringify(searchIndex, null, 2));
console.log(`Search index generated in ${OUTPUT_FILE}`);
