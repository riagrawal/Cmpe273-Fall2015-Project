import md5

class ConsitentHashing(object):

    def __init__(self, nodes=None, replicas=3):
        self.replicas = replicas
        self.ring = dict()
        self._sorted_keys = []
        if nodes:
            for node in nodes:
                self.add_node(node)

    def add_node(self, node):
        ##Addition of new node to the existing hash ring
        for i in xrange(0, self.replicas):
            key = self.get_hash('%s:%s' % (node, i))
            self.ring[key] = node
            self._sorted_keys.append(key)

        self._sorted_keys.sort()

    def delete_node(self, node):
        ##Removes node from hsah ring and all its replicas
        for i in xrange(0, self.replicas):
            key = self.get_key('%s:%s' % (node, i))
            del self.ring[key]
            self._sorted_keys.remove(key)

    def get_node_port(self, string_key):
        ##Returns the corresponding node in hash ring
        return self.get_node_position(string_key)[0]

    def get_node_position(self, string_key):
        ##For a given key , this function returns the corresponding node in hash ring
        ##with its position in the hash ring
        ## it returns NULL if hash ring is empty
        if not self.ring:
            return None, None

        key = self.get_hash(string_key)

        nodes = self._sorted_keys
        for i in xrange(0, len(nodes)):
            node = nodes[i]
            if key <= node:
                return self.ring[node], i

        return self.ring[nodes[0]], 0

    def get_all_nodes(self, string_key):
        ##This function returns the node selected to hold the hash key value pair
        if not self.ring:
            yield None, None

        node, pos = self.get_node_pos(string_key)
        for key in self._sorted_keys[pos:]:
            yield self.ring[key]

        while True:
            for key in self._sorted_keys:
                yield self.ring[key]

    def get_hash(self, key):
        ##Given input key , this function returns a long value that tells the position of node on hash ring
        ## We used MD5 for better and non-conflicting results
        m = md5.new()
        m.update(key)
        return long(m.hexdigest(), 16)
